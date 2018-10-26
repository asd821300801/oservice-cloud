cd %~dp0

call mvn -U clean install -Dmaven.test.skip=true

pause