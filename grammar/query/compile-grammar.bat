cd ../..
set classpath=%classpath%;lib/antlr-2.7.5.jar
rem with debug
rem java antlr.Tool -o src/com/neodatis/knowledger/graph/parser -trace grammar/graph.g

rem without debug
java antlr.Tool -o src/com/neodatis/knowledger/parser/query grammar/query/query.g
java antlr.Tool -o grammar/query/doc -html grammar/query/query.g

