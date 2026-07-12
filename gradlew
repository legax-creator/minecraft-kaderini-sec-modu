#!/usr/bin/env sh

# Sürüm uyumluluğu için standart Gradle Wrapper betiği
DIRNAME=$(dirname "$0")
XIMAGE=$(cd "$DIRNAME" && pwd)
APP_BASE_NAME=$(basename "$0")
APP_HOME="$XIMAGE"

# Java çalıştırma parametreleri
DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

# İşletim sistemi kontrolü ve çalıştırma emri
CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

# Gradle'ı tetikler
if [ -n "$JAVA_HOME" ] ; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD="java"
fi

exec "$JAVACMD" $DEFAULT_JVM_OPTS $GRADLE_OPTS "-classpath" "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"

