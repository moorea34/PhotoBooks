#!/bin/bash

MYCLASSPATH=$CLASSPATH:./libs/jsqldb.jar:./libs/junit.jar:./libs/hsqldb.jar:./libs/mockito-all-1.9.5.jar:./libs/org.eclipse.core.commands_3.6.2.v20130123-162658.jar:./libs/org.eclipse.equinox.common_3.6.100.v20120522-1841.jar:./libs/org.eclipse.jface_3.8.102.v20130123-162658.jar:./libs/org.eclipse.jface.databinding_1.6.0.v20120912-132807.jar:./libs/org.eclipse.jface.databinding.source_1.6.0.v20120912-132807.jar:./libs/org.eclipse.jface.source_3.8.102.v20130123-162658.jar:./libs/org.eclipse.jface.text_3.8.2.v20121126-164145.jar:./libs/org.eclipse.jface.text.source_3.8.2.v20121126-164145.jar:./libs/org.eclipse.osgi_3.8.2.v20130124-134944.jar:./libs/org.eclipse.ui.workbench_3.104.0.v20130204-164612.jar:./libs/swt.jar:./libs/atr.jar:./libs/commons-logging-1.2.jar:./libs/jempbox-1.8.6.jar:./libs/fontbox-1.8.6.jar:./libs/pdfbox-1.8.6.jar

java -XstartOnFirstThread -classpath "$MYCLASSPATH" photobooks.application.PhotoBooks
