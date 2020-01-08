package org.neodatis.nlp.core;

import java.util.List;

import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.gui.tool.populator.IPluggableAutomaticPopulator;


public class NLPAutomaticPopulator implements IPluggableAutomaticPopulator {

	public void execute(int index, List unknownEntities, List availableConnectors, List availableEntities, List definedConnectors, List definedEntities) {
		
		Connector instanceOf = (Connector) availableConnectors.get(0);
		//DLogger.debug(index + " - analysing " + unknownEntities.get(index));
		
		for(int i=0;i<availableEntities.size();i++){
			if(availableEntities.get(i).toString().equals("Verb")){
				if(checkIfitCanBeAVerb(unknownEntities.get(index).toString())){
					definedEntities.set(index,availableEntities.get(i));
					definedConnectors.set(index,instanceOf);
				}
				return;
			}
		}
	}
	
	private boolean checkIfitCanBeAVerb(String entity){
		String [] ends = {"ir","ar","er","ava","avam","aram","ida","ou","ido","iam","endo","ando"};
		
		for(int i=0;i<ends.length;i++){
			if(entity.endsWith(ends[i])){
				return true;
			}
		}
		return false;
	}

}
