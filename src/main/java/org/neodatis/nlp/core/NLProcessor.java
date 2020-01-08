/* 
 * $RCSfile: NLProcessor.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.nlp.core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.neodatis.knowledger.tool.Tool;
import org.neodatis.tool.DLogger;


public class NLProcessor {

    public static Logger logger = Logger.getLogger(NLProcessor.class);

    public static String N = "N";

    public static String V = "V";

    public static String A = "A";

    public static String P = "P";

    public static String ADJ = "ADJ";

    public static String ADV = "ADV";

    public static String PREP = "PREP";

    public static String U = "U";

    protected List nouns;

    protected List verbs;

    protected List articles;

    protected List pronouns;

    protected List adverbs;

    protected List adjectives;

    protected List prepositions;

    protected List unknowns;

    protected Map grammarElements;

    protected List productions;

    private List sentences;

    private List learntElements;

    public NLProcessor() throws Exception {
        init();
    }

    protected void init() throws Exception {
    	//DLogger.register(logger);
    	
        nouns = new ArrayList();
        verbs = new ArrayList();
        articles = new ArrayList();
        pronouns = new ArrayList();
        adverbs = new ArrayList();
        adjectives = new ArrayList();
        prepositions = new ArrayList();
        productions = new ArrayList();
        sentences = new ArrayList();
        unknowns = new ArrayList();
        grammarElements = new HashMap();
        learntElements = new ArrayList();

        grammarElements.put(N, nouns);
        grammarElements.put(V, verbs);
        grammarElements.put(A, articles);
        grammarElements.put(P, pronouns);
        grammarElements.put(ADJ, adjectives);
        grammarElements.put(ADV, adverbs);
        grammarElements.put(PREP, prepositions);
        grammarElements.put(U, unknowns);

        populate();
    }

    protected void populate() {
        nouns.add("olivier");
        nouns.add("carro");
        nouns.add("comida");
        nouns.add("cadeira");
        nouns.add("chave");
        nouns.add("pizza");

        verbs.add("estar");
        verbs.add("esta");
        verbs.add("está");
        verbs.add("ser");
        verbs.add("é");
        verbs.add("são");
        verbs.add("ir");
        verbs.add("vai");
        verbs.add("foi");
        verbs.add("comer");
        verbs.add("come");
        verbs.add("andar");
        verbs.add("anda");
        verbs.add("cair");

        articles.add("a");
        articles.add("o");
        articles.add("as");
        articles.add("os");

        pronouns.add("de");
        pronouns.add("do");
        pronouns.add("da");
        pronouns.add("dos");
        pronouns.add("das");

        adjectives.add("muito");
        adjectives.add("muita");
        adjectives.add("pouco");
        adjectives.add("pouca");
        adjectives.add("rápido");
        adjectives.add("rápida");

        prepositions.add("no");
        prepositions.add("na");
        prepositions.add("nos");
        prepositions.add("nas");

        adverbs.add("rapidamente");

        productions.add("A N V");

    }

    public String display() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("nouns   :").append(nouns);
        buffer.append("\nverbs   :").append(verbs);
        buffer.append("\narticles:").append(articles);
        buffer.append("\npronouns:").append(pronouns);
        buffer.append("\nadjectiv:").append(adjectives);
        buffer.append("\nadverbs :").append(adverbs);
        buffer.append("\npreposit:").append(prepositions);
        buffer.append("\nunknown :").append(unknowns);
        buffer.append("\nnew     :").append(learntElements);

        buffer.append("\ngrammar:");
        for (int i = 0; i < productions.size(); i++) {
            buffer.append("\nP").append(i).append(":").append(productions.get(i));
        }
        /*
         * buffer.append("\nsentences:"); for(int i=0;i<sentences.size();i++){
         * buffer.append("\n").append(i).append(":").append(sentences.get(i)); }
         */
        return buffer.toString();
    }

    public void parseDirectory(String directoryName) {
        DLogger.info("Parsing directory " + directoryName);
        File directory = new File(directoryName);
        File[] files = directory.listFiles();
        for (int i = 0; i < files.length; i++) {
        	if(files[i].isFile()){
        		parseFile(files[i].getAbsolutePath());
        	}            
        }

    }

    public void parseFile(String fileName) {
        DLogger.info("Parsing file " + fileName);
        String text = Tool.getStringFromFile(fileName);
        StringTokenizer stringTokenizer = new StringTokenizer(text, ".;:");
        long numberOfSentences = stringTokenizer.countTokens();
        long i = 1;
        while (stringTokenizer.hasMoreElements()) {
            DLogger.info("sentence " + i++ + "/" + numberOfSentences);
            newInput((String) stringTokenizer.nextElement());
        }
    }

    public void newInput(String text) {
        text = executeTextPreprocessor(text);
        if(text.length()==0){
            return;
        }
        DLogger.debug("Parsing input " + text);

        TextInformation textInformation = new TextInformation(text);
        textInformation = findTags(textInformation);

        DLogger.debug("After Tagging " + textInformation.getTags());
        String production = getProductionFromTaggedText(textInformation);
        storeSentence(text, production);
    }

    private String executeTextPreprocessor(String text) {
        text = text.toLowerCase();
        text = text.replaceAll("\n", " ");
        text = text.replaceAll("\t", " ");
        text = text.replaceAll("\r", " ");
        text = text.replaceAll("  ", " ");
        text = text.replaceAll(" -", " ");
        text = text.replaceAll("!", " ");
        return text;       
    }

    private String getProductionFromTaggedText(TextInformation textInformation) {
        /*
         * boolean taggingIsFull = tagIsFull(taggedSentence); boolean
         * productionExist = productions.indexOf(taggedSentence) != -1;
         */
        return searchForMatchingProduction(textInformation);
    }

    /**
     * Returns true if it does not contain U
     * 
     * @param taggedText
     * @return
     */
    protected boolean tagIsFull(String taggedText) {
        return taggedText.indexOf(" " + U + " ") == -1;
    }

    /**
     * returns the number of undefined tokens in the text
     * 
     * @param taggedText
     * @return
     */
    protected int getNumberOfUndefinedTokens(String taggedText) {
        int initialSize = taggedText.length();
        int newSize = taggedText.replaceAll(U, "").length();

        return initialSize - newSize;

    }

    /** Tags the text
     *  
     * @param textInformation
     * @return
     */
    private TextInformation findTags(TextInformation textInformation) {
        StringTokenizer tokenizer = new StringTokenizer(textInformation.getOriginalText(), " ,'");
        String word = null;

        List tags = new ArrayList();
        List words = new ArrayList();
        
        while (tokenizer.hasMoreElements()) {
            word = tokenizer.nextToken();
            words.add(word);
            tags.add(getGrammarType(word));
        }

        textInformation.setWords(words);
        textInformation.setTags(tags);
        return textInformation;
    }
    
    /**return an array with [A,N,V] when receiving a string 'A N V'
     * 
     * @param production
     * @return
     */
    private List getTagsFromProduction(String production) {
        StringTokenizer tokenizer = new StringTokenizer(production, " ");
        String tag = null;

        List tags = new ArrayList();
        
        while (tokenizer.hasMoreElements()) {
            tag = tokenizer.nextToken();
            tags.add(tag);
        }

        return tags;
    }
    

    private String getGrammarType(String word) {
        Set keys = grammarElements.keySet();
        String key = null;
        List list = null;
        Iterator iterator = keys.iterator();

        while (iterator.hasNext()) {
            key = (String) iterator.next();
            list = (List) grammarElements.get(key);
            if (list.contains(word)) {
                return key;
            }
        }
        unknowns.add(word);
        return U;
    }

    private String searchForMatchingProduction(TextInformation textInformation) {
        String oneProduction = null;
        List productionTags = null;

        // For each production
        for (int i = 0; i < productions.size(); i++) {
            oneProduction = (String) productions.get(i);
            productionTags = getTagsFromProduction(oneProduction);

            // If the size is equal, there is a chance to be compatible
            if (productionTags.size() == textInformation.getTags().size()) {
                for (int j = 0; j < productionTags.size(); j++) {
                    if (    !productionTags.get(j).equals(U) && 
                            !textInformation.getTags().get(j).equals(U) && 
                            !productionTags.get(j).equals(textInformation.getTags().get(j))) {
                        break;
                    }
                    
                    DLogger.debug("Number of undefined tokens for " + textInformation.getTagsAsString() +"="+textInformation.getNumberOfUndefinedTokens() );
                    if(textInformation.getNumberOfUndefinedTokens()>1){
                        break;
                    }
                    // If there is no undefined tags in grammar and if the text
                    // tag is undefined(U) and the grammar is defined
                    // then we can suppose that the undefined text token can be
                    // set to the production token
                    // Example : the text is 'aisa eats food' tagged as U V N.
                    // Aisa is not defined. If there exists a production like N
                    // V N
                    // we can assume that aisa is a N > noun.
                    // getNumberOfUndefinedTokens(oneProduction) == 0 : the
                    // production is totally defined
                    // getNumberOfUndefinedTokens(taggedText) == 1 : The tag
                    // text is considered to map the production only when the
                    // number of U is 1.

                    if (    getNumberOfUndefinedTokens(oneProduction) == 0 // if grammar is 100% 
                            && getNumberOfUndefinedTokens(textInformation.getTags().toString()) == 1 // if text tags have one Undefined tag
                            && textInformation.getTags().get(j).equals(U) && 
                            !productionTags.get(j).equals(U)) {
                        // a token was Undefined, but the matching production is
                        // defining the token, so we can learn the grammar type
                        // of the token
                        // example : original text the car stops, tagged text is
                        // A U V, and it matches the A N V produciton, we can
                        // learn that car is N(noun)
                        learnWord((String) textInformation.getWords().get(j), (String) productionTags.get(j), textInformation.getOriginalText(), oneProduction);
                    }
                }
                DLogger.debug("Matching grammar found : " + productions.get(i).toString());
                return (String) productions.get(i);
            }
        }
        DLogger.debug("No matching production exist for " + textInformation.getTags() );
        learnProductionFromTaggedText(textInformation);
        return null;
    }

    protected void learnWord(String word, String grammarType, String originalText, String production) {

        // first tries to remove from unknowns
        unknowns.remove(word);
        ((List) grammarElements.get(grammarType)).add(word);
        DLogger.info("Learning that " + word + " is a " + grammarType);
        learntElements.add(word + " is a " + grammarType + "(" + originalText + ":" + production + ")");
    }

    protected void learnProductionFromTaggedText(TextInformation textInformation) {

        int numberOfTags = textInformation.getTags().size();
        int numberOfUndefinedTags = textInformation.getNumberOfUndefinedTokens();
        
        if(numberOfTags>1 && numberOfUndefinedTags == 0){
            productions.add(textInformation.getTagsAsString());
            DLogger.info("Learning production :" + textInformation.getTagsAsString());
        }
    }

    protected void storeSentence(String text, String production) {
        // sentences.add(text + " : " + production);
    }

    public static void main(String[] args) throws Exception {
        NLProcessor processor = new NLProcessor();
        System.out.println(processor.display());
        processor.newInput("a pedra voa");
        System.out.println(processor.display());
        processor.parseDirectory("c:/tmp/texts");
        System.out.println(processor.display());
    }

	public List getSentences() {
		return sentences;
	}

	public List getUnknowns() {
		return unknowns;
	}
}
