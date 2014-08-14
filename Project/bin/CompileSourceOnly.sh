#!/bin/bash

MYCLASSPATH=$CLASSPATH:./:./bin/:./bin/libs/jsqldb.jar:./bin/libs/junit.jar:./bin/libs/hsqldb.jar:./bin/libs/mockito-all-1.9.5.jar:./bin/libs/org.eclipse.core.commands_3.6.2.v20130123-162658.jar:./bin/libs/org.eclipse.equinox.common_3.6.100.v20120522-1841.jar:./bin/libs/org.eclipse.jface_3.8.102.v20130123-162658.jar:./bin/libs/org.eclipse.jface.databinding_1.6.0.v20120912-132807.jar:./bin/libs/org.eclipse.jface.databinding.source_1.6.0.v20120912-132807.jar:./bin/libs/org.eclipse.jface.source_3.8.102.v20130123-162658.jar:./bin/libs/org.eclipse.jface.text_3.8.2.v20121126-164145.jar:./bin/libs/org.eclipse.jface.text.source_3.8.2.v20121126-164145.jar:./bin/libs/org.eclipse.osgi_3.8.2.v20130124-134944.jar:./bin/libs/org.eclipse.ui.workbench_3.104.0.v20130204-164612.jar:./bin/libs/swt.jar:./bin/libs/atr.jar:./bin/libs/commons-logging-1.2.jar:./bin/libs/jempbox-1.8.6.jar:./bin/libs/fontbox-1.8.6.jar:./bin/libs/pdfbox-1.8.6.jar

javac -d ./bin/ -classpath "$MYCLASSPATH" ./photobooks/application/*.java ./photobooks/business/*.java ./photobooks/gateways/*.java ./photobooks/objects/*.java ./photobooks/presentation/*.java
