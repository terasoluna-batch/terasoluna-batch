set TESTNO=%~1
call common.bat
set MAIN=common.Main28_33$35
set CLASSPATH=%TESTDIR%\extjar\extjar.jar;%CLASSPATH%
call compile.bat
call run.bat