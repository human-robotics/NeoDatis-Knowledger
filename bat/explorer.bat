cd ..
set cp=lib/neodatis-odb.jar;lib/log4j-1.2.8.jar;lib/prefuse.jar;lib/antlr-2.7.5.jar;dist/knowledger.jar 
echo CLASSPATH=%cp%
java -Dlanguage=br -cp %cp% org.neodatis.knowledger.gui.graph.Main
                            
