
call setClasspath

cd database
call RestoreDB.bat
cd ..

java junit.swingui.TestRunner tests.UnitTests

pause

