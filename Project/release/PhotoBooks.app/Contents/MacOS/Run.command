#!/bin/bash

cd "${0%/*}"

java -XstartOnFirstThread -classpath "./:." -jar ./PhotoBooks1.0.0.jar
