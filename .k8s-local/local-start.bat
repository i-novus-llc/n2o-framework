cd ..
start mvn clean install -DskipTests=true -Pfrontend,frontend-build,backend,!examples,jacoco,demo,docs,sandbox
echo Please wait mvn clean install
pause
cd .k8s-local/target/
call local-helm.bat
