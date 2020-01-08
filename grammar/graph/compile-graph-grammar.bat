cd ../..
set classpath=%classpath%;lib/antlr-2.7.5.jar
rem with debug
rem java antlr.Tool -o src/com/neodatis/knowledger/graph/parser -trace grammar/graph.g

rem without debug
java antlr.Tool -o src/com/neodatis/knowledger/parser/graph grammar/graph/graph.g
java antlr.Tool -o grammar/graph/doc -html grammar/graph/graph.g

