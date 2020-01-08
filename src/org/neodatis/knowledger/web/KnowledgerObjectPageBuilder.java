package org.neodatis.knowledger.web;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.KnowledgerObject;
import org.neodatis.knowledger.core.implementation.entity.Relation;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;

public class KnowledgerObjectPageBuilder {
	private long id;
	private String baseName;
	public KnowledgerObjectPageBuilder(String baseName,String sId){
		this.id = Long.parseLong(sId);
		this.baseName = baseName;
	}
	
	public String buildHtml(){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<html><body>");
		KnowledgeBase kb;
		try {
			kb = KnowledgeBaseFactory.getInstance(baseName,KnowledgeBaseType.ODB);
			KnowledgerObject ko = kb.getObjectFromId(id);
			buffer.append("<h2>name=").append(ko.getIdentifier()).append("</h2><hr>");
			
			buffer.append("<h3>Relations=").append("<h3>");
			buffer.append(buildTable(kb.getRelationsOf(ko))).append("<br><br>");
			if(ko instanceof Concept){
				Concept concept = (Concept) ko;
				buffer.append("<h3>Cached Relations=").append("<h3>");
				buffer.append(buildTable(concept.getRelationList())).append("<br>");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			buffer.append("<pre>").append(e.getMessage()).append("</pre>");
		}
		buffer.append("</body></html>");
		
		return buffer.toString();
	}
	String buildTable(RelationList relations){
		Relation relation = null;
		StringBuffer buffer = new StringBuffer();
		buffer.append("<table>");
		for(int i=0;i<relations.size();i++){
			relation = relations.getRelation(i);
			buffer.append("<tr>");
			buffer.append("<td>").append(relation.getLeftEntity().getIdentifier()).append("</td>");
			buffer.append("<td>").append(relation.getConnector().getIdentifier()).append("</td>");
			buffer.append("<td>").append(relation.getRightEntity().getIdentifier()).append("</td>");
			buffer.append("</tr>");
		}
		buffer.append("</table>");
		return buffer.toString();
	}

}
