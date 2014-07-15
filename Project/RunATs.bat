REM @echo off

IF "%1" NEQ "" (SET SLEEP=%1) ELSE (SET SLEEP=1)

call setClasspath

cd database
call RestoreDB.bat
cd ..

java -cp %CLASSPATH% acceptanceTests.TestRunner %SLEEP%

set SLEEP=

pause


