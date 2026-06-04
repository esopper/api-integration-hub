#!/bin/bash
set -e

HOST=${1:?"Usage: deploy.sh <host>"}
JAR=$(ls build/libs/api-integration-hub-*.jar | head -1)

echo "Building..."
./gradlew bootJar

echo "Copying JAR to $HOST..."
scp "$JAR" "ec2-user@$HOST:/tmp/application.jar"

echo "Installing..."
ssh "ec2-user@$HOST" bash <<'EOF'
  sudo mv /tmp/application.jar /opt/api/application.jar
  sudo systemctl restart api
  sudo systemctl status api --no-pager
EOF

echo "Done."
