mvn clean compile test package

docker login -u vosmerkin
docker build -f xDockerfile.txt -t vosmerkin/pet_shelter:latest .
docker push vosmerkin/pet_shelter:latest

#echo copy files to suse
#scp target/marketplace-0.0.1-SNAPSHOT.jar evgen@192.168.31.61:/home/evgen/pet_shelter

echo ssh to suse
ssh evgen@192.168.31.61 << EOF
  cd pet_shelter

  echo docker compose up
  docker compose pull
  docker compose up -d
  #if [ ! "$(docker ps -a -q -f name=postgres_db_marketplace)" ]; then
  #    if [ "$(docker ps -aq -f status=exited -f name=postgres_db_marketplace)" ]; then
  #        # cleanup
  #        docker rm postgres_db_marketplace
  #    fi
  #    # run your container
  #    docker run -d --name <name> my-docker-image
  #fi

  #echo run app
  #pkill -f marketplace-0.0.1-SNAPSHOT.jar || echo "No running Java app found, continuing..."
  #nohup java -jar -Dspring.profiles.active=prod marketplace-0.0.1-SNAPSHOT.jar > app.log 2>&1 &

  source env_vars.sh
  #source rerun_in_normal.sh

  echo "✅ Deployment complete! App is running in the background."
EOF