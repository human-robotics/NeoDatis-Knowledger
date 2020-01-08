package org.neodatis.nlp.core;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple class to map a string and its tagged information
 * 
 * @author olivier s
 * 
 */
public class TextInformation {
    private String originalText;

    private List words;
    private List tags;

    public TextInformation(String originalText) {
        init(originalText);
    }

    protected void init(String originalText) {
        this.originalText = originalText;
        words = new ArrayList();
        tags = new ArrayList();
    }

    /**
     * @return Returns the originalText.
     */
    public String getOriginalText() {
        return originalText;
    }

    /**
     * @param originalText The originalText to set.
     */
    public void setOriginalText(String originalText) {
        this.originalText = originalText;
    }

    /**
     * @return Returns the tags.
     */
    public List getTags() {
        return tags;
    }
    
    /**
     * returns the number of undefined tokens in the text
     * 
     * @param taggedText
     * @return
     */
    protected int getNumberOfUndefinedTokens() {
        String taggedText = getTagsAsString();
        int initialSize = taggedText.length();
        int newSize = taggedText.replaceAll(NLProcessor.U, "").length();

        return initialSize - newSize;

    }

    
    public String getTagsAsString(){
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<tags.size();i++){
            buffer.append(tags.get(i));
            if(i<tags.size()){
                buffer.append(" ");
            }
        }
        return buffer.toString();
        
    }

    /**
     * @param tags The tags to set.
     */
    public void setTags(List tags) {
        this.tags = tags;
    }

    /**
     * @return Returns the words.
     */
    public List getWords() {
        return words;
    }

    /**
     * @param words The words to set.
     */
    public void setWords(List words) {
        this.words = words;
    }

    
}
