set cp=lib/neodatis-odb.jar;lib/log4j-1.2.8.jar;lib/prefuse.jar;lib/antlr-2.7.5.jar;knowledger.jar 
echo CLASSPATH=%cp%
java -cp %cp% org.neodatis.knowledger.gui.graph.Main
                            
