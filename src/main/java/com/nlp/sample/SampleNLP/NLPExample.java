package com.nlp.sample.SampleNLP;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.parser.Parser;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class NLPExample {
	public static void main(String args[]){ 
		
		String sentence = " Hi. How are you? I am absolutely fine. " 
		         + "How was your day today? It was Great.";
		String nameSentence = " Ram and Shayam went to London."
				+ "They went there by aeroplane. " 
		         + "They spend 2 days and 3 night.";
		
		SentenceModel model = null;
		POSModel posModel = null;
		TokenizerModel tokenModel = null;
		ParserModel parseRawTextModel = null;
		TokenNameFinderModel tokenNameModel = null;
		TokenNameFinderModel tokenAddressModel = null;
		
		try {
			//Loading sentence detector model 
			InputStream inputStream = new FileInputStream("E:/ApacheNLP/ensent.bin");
			model = new SentenceModel(inputStream);
			
			//Loading the Tokenizer model 
			InputStream inputStreamForToken = new FileInputStream("E:/ApacheNLP/en-token.bin");
			tokenModel = new TokenizerModel(inputStreamForToken);
			
			//Loading the Tokenizer name model 
			InputStream inputStreamForName = new FileInputStream("E:/ApacheNLP/en-ner-person.bin");
			tokenNameModel = new TokenNameFinderModel(inputStreamForName);
			
			//Loading the Tokenizer Address model 
			InputStream inputStreamForAddress = new FileInputStream("E:/ApacheNLP/en-ner-location.bin");
			tokenAddressModel = new TokenNameFinderModel(inputStreamForAddress);
			
			//Loading Parts of speech-maxent model : Parts of speech
			InputStream inputStreamForPOS = new FileInputStream("E:/ApacheNLP/en-pos-maxent.bin"); 
			posModel = new POSModel(inputStreamForPOS);
			
			//Parsing raw text : Loading parser model 
			InputStream inputStreamRawText = new FileInputStream("E:/ApacheNLP/en-parser-chunking.bin"); 
			parseRawTextModel = new ParserModel(inputStreamRawText); 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		//Instantiating the SentenceDetectorME class 
		SentenceDetectorME detector = new SentenceDetectorME(model);
		
		//Detecting the sentence 
		String sentences[] = detector.sentDetect(sentence);
		
		//Printing the sentences 
	      for(String sent : sentences)        
	         System.out.println(sent);   
		
	      System.out.println("\n------------------------------------------------------------------\n");
	      
		//Detecting the position of the sentences in the paragraph  
		Span[] spans = detector.sentPosDetect(sentence); 
		
		//Printing the sentences and their spans of a sentence 
		for (Span span : spans)         
		System.out.println(sentence.substring(span.getStart(), span.getEnd())+" "+ span); 
	     
		 System.out.println("\n------------------------------------------------------------------\n");
	      
		//Getting the probabilities of the last decoded sequence       
	    double[] probs = detector.getSentenceProbabilities(); 
	       
	    for(double prob : probs) 
	         System.out.println(prob); 
	    
	    System.out.println("\n------------------------------------------------------------------\n");
	      
	    /**
	     *  Start Tokenizing 
	     *  Three type of Tokenizer is available:
	     *  1) Simple Tokenizer
	     *  2) Whitespace Tokenizer
	     *  3) TokenizerME
	     */
	    SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE; 
	  
	    //Tokenizing the given sentence 
	    String simpleTokens[] = simpleTokenizer.tokenize(sentence);  
	       
	    //Printing the tokens 
	    for(String token : simpleTokens) {         
	       System.out.println(token);  
	      }    
	    
	    System.out.println("\n------------------------------------------------------------------\n");
	    
	    //Instantiating the TokenizerME class 
	    TokenizerME tokenizer = new TokenizerME(tokenModel);
	    
	    //Tokenizing the given raw text 
	    String rawTokens[] = tokenizer.tokenize(sentence);
	    
	    //Printing the tokens  
	    for (String a : rawTokens) 
	         System.out.println(a); 
	    
	    System.out.println("\n------------------------------------------------------------------\n");
	    
	    //Retrieving the tokens 
	    Span[] tokens = tokenizer.tokenizePos(sentence);
	    
	    //Printing the spans of tokens 
	    for( Span token : tokens)        
	    	 System.out.println(token +" "+sentence.substring(token.getStart(), token.getEnd()));
	     
	    System.out.println("\n------------------------------------------------------------------\n");
	    
	    //Getting the probabilities of the recent calls to tokenizePos() method 
	    double[] probabs = tokenizer.getTokenProbabilities();
	    
	    for(double prob : probabs) 
	         System.out.println(prob); 
	    
	    System.out.println("\n------------------------------------------------------------------\n");
	    
		// Instantiating the NameFinderME class
		NameFinderME nameFinder = new NameFinderME(tokenNameModel);
		
		String rawTokens1[] = tokenizer.tokenize(nameSentence);
		
		// Finding the names in the sentence
		Span nameSpans[] = nameFinder.find(rawTokens1);

		// Printing the names and their spans in a sentence
		for (Span s : nameSpans)
			System.out.println(s.toString() + "  " + rawTokens1[s.getStart()]);

		
		System.out.println("\n------------------------------------------------------------------\n");
	    
		
		// Instantiating the NameFinderME class
		NameFinderME addFinder = new NameFinderME(tokenAddressModel);

		String rawTokens2[] = tokenizer.tokenize(nameSentence);

		// Finding the names of a location
		Span addSpans[] = addFinder.find(rawTokens2);
		
		// Printing the spans of the locations in the sentence
		for (Span s : addSpans)
			System.out.println(s.toString() + "  " + rawTokens2[s.getStart()]);
		
		System.out.println("\n------------------------------------------------------------------\n");
		
		//Instantiating POSTaggerME class 
		POSTaggerME tagger = new POSTaggerME(posModel);
		
		String rawTokens3[] = tokenizer.tokenize(nameSentence);
		
		//Generating tags 
		String[] tags = tagger.tag(rawTokens3); 
		
		//Instantiating the POSSample class 
		POSSample sample = new POSSample(rawTokens3, tags); 
		System.out.println(sample.toString());
		
		System.out.println("\n------------------------------------------------------------------\n");
		
		/**
		 * to parse raw text using OpenNLP API
		 */
		// Creating a parser
		Parser parser = ParserFactory.create(parseRawTextModel);

		// Parsing the sentence
		Parse topParses[] = ParserTool.parseLine(nameSentence, parser, 1);

		for (Parse p : topParses)
			p.show();

	}
}
