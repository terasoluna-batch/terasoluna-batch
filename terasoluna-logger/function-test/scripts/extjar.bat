call common.bat
del %EXTJARDIR%\extjar.jar
cd %EXTJARDIR%
javac -d %EXTJARDIR% -cp %CLASSPATH% %EXTJARDIR%\ext\ExtLib.java
jar cvf extjar.jar * 
cd %TESTDIR%\scripts