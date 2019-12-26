#!/bin/sh

export JAVA_OPTS="-Djava.awt.headless=false"
kotlinc-jvm -cp build/libs/toyGraphics-demos-all.jar
