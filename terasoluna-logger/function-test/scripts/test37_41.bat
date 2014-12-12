set TESTNO=%~1
call common.bat
set MAIN=common.Main37_41
set CLASSPATH=%TESTDIR%\extjar\extjar.jar;%CLASSPATH%
call compile.bat
call run.bat