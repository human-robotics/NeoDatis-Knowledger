/* 
 * $RCSfile: DialogyNLProcessor.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:28 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.nlp.core;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Instance;
import org.neodatis.knowledger.core.implementation.entity.InstanceList;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.query.RelationQuery;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;

public class DialogyNLProcessor extends NLProcessor{
    private KnowledgeBase knowledgeBase;
    private boolean updateKnowledgeBase;
    public DialogyNLProcessor(KnowledgeBase knowledgeBase, boolean updateKnowledgeBase) throws Exception{
        super();
        this.updateKnowledgeBase = updateKnowledgeBase;
        this.knowledgeBase = knowledgeBase;
    }
    
    protected void populate()  {
        try{
            nouns.addAll(getConcept(N).getInstances().getIdentifiers());
            verbs.addAll(getConcept(V).getInstances().getIdentifiers());
            articles.addAll(getConcept(A).getInstances().getIdentifiers());
            pronouns.addAll(getConcept(P).getInstances().getIdentifiers());
            prepositions.addAll(getConcept(PREP).getInstances().getIdentifiers());
            adverbs.addAll(getConcept(ADV).getInstances().getIdentifiers());
            adjectives.addAll(getConcept(ADJ).getInstances().getIdentifiers());
            unknowns.addAll(getConcept(U).getInstances().getIdentifiers()); 
            
            Instance grammar = knowledgeBase.getInstanceFromValue("portugueseGrammar",GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
            Connector connector = knowledgeBase.getConnectorFromName("is-production-of",GetMode.THROW_EXCEPTION_IF_DOES_NOT_EXIST);
            
            RelationQuery query = new RelationQuery();
            query.addConnectorToInclude(connector);
            query.addRightEntityToInclude(grammar);
            
            RelationList relationList = query.executeOn(knowledgeBase.getRelationList());
            InstanceList instanceList = new InstanceList(knowledgeBase,relationList.getLeftEntities());
            productions.addAll(instanceList.getValues());       
            
        }catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    /* (non-Javadoc)
     * @see org.neodatis.nlp.NLProcessor#learn(java.lang.String, java.lang.String)
     */
    protected void learnWord(String word, String grammarType, String originalText, String production) {
        super.learnWord(word, grammarType, originalText, production);
        if(updateKnowledgeBase){
            try {
            	getConcept(grammarType).newInstance(word);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    
    /* (non-Javadoc)
     * @see org.neodatis.nlp.NLProcessor#learnProduction(java.lang.String)
     */
    protected void learnProductionFromTaggedText(TextInformation textInformation) {
        super.learnProductionFromTaggedText(textInformation);
        if(updateKnowledgeBase){
            try {
                Concept grammarProduction = knowledgeBase.getConceptFromName("GrammarProduction",GetMode.CREATE_IF_DOES_NOT_EXIST);
                Instance p = grammarProduction.newInstance(textInformation.getTagsAsString());
                p.connectTo("is-production-of",knowledgeBase.getInstanceFromValue("portugueseGrammar",GetMode.CREATE_IF_DOES_NOT_EXIST));
            } catch (Exception e) {
                e.printStackTrace();
            }        
        }
    }

    Concept getConcept(String grammarType) throws Exception{
        
        try{
        	Concept base = knowledgeBase.getConceptFromName("GrammaticalEntity",GetMode.CREATE_IF_DOES_NOT_EXIST);
            if(grammarType.equals(N)){
                return base.getSubConcept("Noun");
            }
            if(grammarType.equals(V)){
                return base.getSubConcept("Verb");
            }
            if(grammarType.equals(A)){
                return base.getSubConcept("Article");
            }
            if(grammarType.equals(P)){
                return base.getSubConcept("Pronoun");
            }
            if(grammarType.equals(ADJ)){
            	base.getSubConcept("Adjective");
            }
            if(grammarType.equals(ADV)){
            	base.getSubConcept("Adverb");
            }
    
            return knowledgeBase.getConceptFromName("UnknownGrammaticalEntity",GetMode.CREATE_IF_DOES_NOT_EXIST);
        }catch(Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) throws Exception {
        DialogyNLProcessor processor = new DialogyNLProcessor(KnowledgeBaseFactory.getInstance("test",KnowledgeBaseType.IN_MEMORY),false);
        System.out.println(processor.display());
        processor.newInput("não tem nada a ver");
        System.out.println(processor.display());
        processor.parseDirectory("c:/tmp/texts");
        processor.parseDirectory("c:/tmp/texts");
        System.out.println(processor.display());
    }

}
