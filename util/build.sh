#!/bin/sh

./gradlew desktop:dist
cp ./desktop/build/libs/desktop-1.0.jar ./util/hauntGame.jar

rm ./out/win/ -r
java -jar ./util/packr-all-4.0.0.jar \
     --platform windows64 \
     --jdk "C:\\Program Files\\Java\\jdk-21" \
     --useZgcIfSupportedOs \
     --executable HauntGame \
     --classpath ./util/hauntGame.jar \
     --mainclass com.haunt.game.DesktopLauncher \
     --vmargs Xmx1G \
     --resources asserts \
     --output out/win

cp -r ./assets ./out/win/assets