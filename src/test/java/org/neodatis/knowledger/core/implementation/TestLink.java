/* 
 * $RCSfile: TestLink.java,v $
 * Tag : $Name:  $
 * $Revision: 1.1 $
 * $Author: osmadja $
 * $Date: 2010-08-01 18:51:29 $
 * 
 * Copyright 2003 Cetip
 */
package org.neodatis.knowledger.core.implementation;

/*
 * Created on 30/06/2006
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

import java.util.ArrayList;
import java.util.List;

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


public class TestLink {

    public static void main(String[] args) throws Exception {
        TestLink tl = new TestLink();
        
        
        for(int i=0;i<5000;i++){
            tl.t1();
            //Thread.sleep(50);
            System.out.println(i);
        }
    }
    public void t2() throws Exception{
        String baseName = "Sistemas de Hipermidia";
        
        ODB odb = ODBFactory.open(baseName);
        odb.close();
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
        Connector connector = kb.getConnectorFromName("e pagina de",                GetMode.CREATE_IF_DOES_NOT_EXIST);
        //RelationList rlist = kb.queryRelations(null, connector, conceitoClicado);
        RelationList rlist = kb.queryRelations(null, null, conceitoClicado);
        rlist.addAll(kb.queryRelations(conceitoClicado,null,null));
        EntityList list = rlist.getLeftEntities();

        // para cada instancia de conteudo
        for (int i = 0; i < list.size(); i++) {
            Entity entidade = (Entity) list.get(i);
            String nomeConteudo = entidade.getIdentifier();
            // pego a situacao das paginas para este aluno
            /*
            Link link = new Link(nomeConteudo);
            // TODO tratar os relacionamento entre as paginas - link e
            // pre-requisito
            link.setPodeVisitar(true);
            link.setVisitado(false);
            linksConceito.add(link);
            */

        }

        //System.out.println("chamando linkbranco com " + linksConceito);
        kb.close();

    }

}