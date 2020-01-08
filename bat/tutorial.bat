rem cd ..
set cp=lib/neodatis-odb.jar;lib/log4j-1.2.8.jar;lib/prefuse.jar;lib/antlr-2.7.5.jar;dist/knowledger.jar;knowledger.jar 
echo CLASSPATH=%cp%
rem java -Dlanguage=br -cp %cp% com.neodatis.knowledger.gui.graph.Main
java -cp %cp% org.neodatis.knowledger.example.HelpDeskExample
                            
