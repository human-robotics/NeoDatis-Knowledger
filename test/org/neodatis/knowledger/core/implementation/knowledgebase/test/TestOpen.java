package org.neodatis.knowledger.core.implementation.knowledgebase.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.neodatis.knowledger.core.factory.KnowledgeBaseFactory;
import org.neodatis.knowledger.core.implementation.entity.Concept;
import org.neodatis.knowledger.core.implementation.entity.Connector;
import org.neodatis.knowledger.core.implementation.entity.Entity;
import org.neodatis.knowledger.core.implementation.entity.EntityList;
import org.neodatis.knowledger.core.implementation.entity.RelationList;
import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.GetMode;
import org.neodatis.odb.main.ODB;
import org.neodatis.odb.main.ODBFactory;


public class TestOpen extends TestCase {
 	public static void main(String[] args) throws Exception {
 		TestOpen to = new TestOpen();
 		
 		
 		for(int i=0;i<5000;i++){
 			to.t3();
 			if(i%100==0){
 				System.out.println("************ counter="+i);
 			}
 			
 		}
 	}

 	public void t1() throws Exception{
 		String baseName = "Sistemas de Hipermidia";
 		long id = 1151211138535L;
 		KnowledgeBase kb = null;
 
 		List linksConceito = new ArrayList();
 		// obtem lista de instancias de conteudo que tem relacionamento e
 		// pagina de com conceito (id) para base (baseName)
 		kb = KnowledgeBaseFactory.getInstance(baseName);
 		//System.out.println("size " + kb.getRelationList().size());
 		Concept conceitoClicado = kb.getConceptFromId(id);
 		//System.out.println("CONCEITO:" + conceitoClicado);
 		Connector connector = kb.getConnectorFromName("e pagina de",GetMode.CREATE_IF_DOES_NOT_EXIST);
 		//RelationList rlist = kb.queryRelations(null, connector, conceitoClicado);
 		RelationList rlist = kb.queryRelations(null, null, conceitoClicado);
 		rlist.addAll(kb.queryRelations(conceitoClicado,null,null));
 		EntityList list = rlist.getLeftEntities();
 
 		// para cada instancia de conteudo
 		for (int i = 0; i < list.size(); i++) {
 			Entity entidade = (Entity) list.get(i);
 			String nomeConteudo = entidade.getIdentifier();
 			// pego a situacao das paginas para este aluno
 			//Link link = new Link(nomeConteudo);
 			// TODO tratar os relacionamento entre as paginas - link e
 			// pre-requisito
 			//link.setPodeVisitar(true);
 			//link.setVisitado(false);
 			//linksConceito.add(link);
 
 		}
 
 		//System.out.println("chamando linkbranco com " + linksConceito);
 		kb.close();
 
 	}
 	
 	public void t2() throws Exception{
 		String baseName = "Sistemas de Hipermidia";
 		long id = 1151211138535L;
 		KnowledgeBase kb = null;
 
 		// obtem lista de instancias de conteudo que tem relacionamento e
 		// pagina de com conceito (id) para base (baseName)
 		kb = KnowledgeBaseFactory.getInstance(baseName);
 		kb.close();
 
 	}
 	public void t3() throws Exception{
 		String baseName = "Sistemas de Hipermidia";
 		long id = 1151211138535L;
 		ODB odb = ODBFactory.open("Sistemas de Hipermidia.knowledger");
 		odb.close();
 	}

}
