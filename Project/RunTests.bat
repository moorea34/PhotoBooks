
call setClasspath

cd database
call RestoreDB.bat
cd ..

REM Running Unit Tests
java junit.swingui.TestRunner tests.UnitTests

timeout 10

REM Running Integration Tests
java junit.swingui.TestRunner tests.IntegrationTests

timeout 10

REM Running Acceptance Tests
java -cp %CLASSPATH% acceptanceTests.TestRunner

pause

