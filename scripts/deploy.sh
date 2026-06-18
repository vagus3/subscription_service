#!/usr/bin/env bash
set -euo pipefail

APP_DIR="${APP_DIR:-/opt/subscription-service}"
JAR_NAME="${JAR_NAME:-subscription-service-1.0.0.jar}"
PROFILE="${SPRING_PROFILES_ACTIVE:-prod}"

cd "$APP_DIR"

if [ ! -f "build/libs/$JAR_NAME" ]; then
  ./gradlew bootJar --no-daemon
fi

pkill -f "$JAR_NAME" || true
nohup java -jar "build/libs/$JAR_NAME" --spring.profiles.active="$PROFILE" > app.log 2>&1 &

echo "Started $JAR_NAME with profile=$PROFILE"
