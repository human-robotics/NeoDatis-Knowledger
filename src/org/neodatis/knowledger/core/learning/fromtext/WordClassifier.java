/* 
 * $RCSfile: WordClassifier.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:29 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.learning.fromtext;

import org.neodatis.knowledger.core.ObjectDoesNotExistException;
import org.neodatis.knowledger.core.factory.InstanceFactory;
import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.IKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;
import org.neodatis.knowledger.tool.Tool;


/**
 * <p>
 * This class is used to classify word. For example, it receives a text, analyse
 * all the words and try to define if it is a noun, an article, a verb , an
 * adjective...
 * </p>
 *  
 */
public class WordClassifier {
	private IKnowledgeBase knowledgeBase;
	public WordClassifier(IKnowledgeBase knowledgeBase){
		this.knowledgeBase = knowledgeBase;
	}
    /**
     * Here we use the following rule. a word which is after an article or a
     * pronoun is a noun. Example: a ball. a is an article => ball is a noun
     * 
     * @param textToAnalyse
     * @param updateKnowledgeBase To know if new nouns must be insert into knowledge base
     * @throws Exception
     * @throws ObjectDoesNotExistException
     */
    public InstanceList extractNouns(String textToAnalyse,boolean updateKnowledgeBase) throws ObjectDoesNotExistException, Exception {
        String word = null;
        String newNoun = null;
        Instance newNounInstance = null;
        Concept article = knowledgeBase.getConceptFromName("Article",GetMode.CREATE_IF_DOES_NOT_EXIST);
        Concept noun = knowledgeBase.getConceptFromName("Noun",GetMode.CREATE_IF_DOES_NOT_EXIST);
        Concept pronoun = knowledgeBase.getConceptFromName("Pronoun",GetMode.CREATE_IF_DOES_NOT_EXIST);
        int index = 0;
        // First gets all article and pronouns
        InstanceList articlesAndPronouns = article.getInstances(1);
        articlesAndPronouns.addAll(pronoun.getInstances(1));

        InstanceList newNouns = new InstanceList();

        // Split the text into words
        String[] words = textToAnalyse.split(" .,:)(;");

        for (int i = 0; i < articlesAndPronouns.size(); i++) {
            word = articlesAndPronouns.getInstance(i).getValue();
            index = 0;
            index = checkPosition(word, words, index);

            while (index != -1) {

                // If the text contains the word (index != -1)
                if (words.length > index) {
                    newNoun = words[index + 1];
                    System.out.println("The word " + newNoun + " seems to be a noun! (" + index + ")");

                    if (!noun.existInstance(newNoun)) {
                        newNounInstance = InstanceFactory.create(newNoun);
                        newNouns.add(newNounInstance);
                    }
                }
                index = checkPosition(word, words, index + 1);
            }

        }
        
        if(updateKnowledgeBase){
        	noun.createInstances(newNouns);
        }
        return newNouns;
    }

    /**
     * @param word
     * @param words
     * @return
     */
    private int checkPosition(String word, String[] words, int currentIndex) {
        for (int i = currentIndex; i < words.length; i++) {
            if (words[i].equalsIgnoreCase(word)) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws ObjectDoesNotExistException, Exception {
        WordClassifier wordClassifier = new WordClassifier(KnowledgeBaseFactory.getInstance("nlp",KnowledgeBaseType.IN_MEMORY));

        String text = Tool.getStringFromFile("D:\\myProjects\\DialogyV2\\text\\dirceu.txt");
        //String text = Tools.getStringFromFile("c:/inputs/europa.txt");
        //text += "\n" + Tools.getStringFromFile("c:/inputs/bolivia.txt");

        InstanceList instanceList = wordClassifier.extractNouns(text,true);
        
        System.out.println(instanceList);
    }
}