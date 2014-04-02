@echo off
REM app\javac -d "BtecTransformer" -cp app\lib\*.jar;. src\com\pearson\BtecTransformer.java
app\java -classpath app\lib\saxon9he.jar;app\lib\logback-classic-1.0.6.jar;app\lib\logback-core-1.0.6.jar;app\lib\slf4j-api-1.7.4.jar;app com.pearson.BtecTransformer