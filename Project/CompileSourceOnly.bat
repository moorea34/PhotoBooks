
cd bin
erase /S /Q *.class
cd ..


call setClasspath

REM Had to compile these together because of dependencies
javac -d bin\ -cp %classpath% src\photobooks\objects\*.java src\photobooks\business\*.java src\photobooks\gateways\*.java src\photobooks\presentation\*.java src\photobooks\application\*.java

pause

