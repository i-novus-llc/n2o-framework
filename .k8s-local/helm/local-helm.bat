set DOCKER_REGISTRY=local

cd ../../
cd .docker-compose/target
docker compose -p n2o-stack -f docker-compose.build.yml build

cd ../../
cd .k8s/target
#--dry-run --debug
helm upgrade -i n2o-stack --set global.dockerRegistry=%DOCKER_REGISTRY% --dependency-update --wait -f values.local.yaml --timeout 20m .
