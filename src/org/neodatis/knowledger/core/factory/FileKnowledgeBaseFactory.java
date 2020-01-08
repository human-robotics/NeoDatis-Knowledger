package org.neodatis.knowledger.core.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.ObjectInputStream;

import org.neodatis.knowledger.core.implementation.knowledgebase.KnowledgeBase;
import org.neodatis.knowledger.core.implementation.knowledgebase.SimpleFileKnowledgeBase;
import org.neodatis.knowledger.core.interfaces.knowledgebase.KnowledgeBaseType;


public class FileKnowledgeBaseFactory {

	public static KnowledgeBase getInstance(String name) throws Exception {
		KnowledgeBase knowledgeBase = null;
		File file = new File(name + ".knowledger");
		if (file.exists()) {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(name + ".knowledger"));
			knowledgeBase = (KnowledgeBase) ois.readObject();
		} else {
			knowledgeBase = new SimpleFileKnowledgeBase(name);
		}
		return knowledgeBase;
	}

	public static KnowledgeBaseDescription[] getAllKnowledgeBaseDescriptions() throws Exception {
		File files = new File(".");
		File[] kbFiles = files.listFiles(new MyFilter());

		KnowledgeBaseDescription[] knowledgeBaseDescriptions = new KnowledgeBaseDescription[kbFiles.length];
		for (int i = 0; i < kbFiles.length; i++) {
			knowledgeBaseDescriptions[i] = new KnowledgeBaseDescription(kbFiles[i].getName().replaceAll(".knowledger",""), KnowledgeBaseType.FILE);
		}
		return knowledgeBaseDescriptions;
	}

}

class MyFilter implements FilenameFilter {

	public boolean accept(File dir, String name) {
		return name.endsWith("knowledger");
	}

}
