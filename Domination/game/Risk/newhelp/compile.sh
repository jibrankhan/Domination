#!/bin/sh
#FIXME: create output directory if it doesn't exist
echo Cleaning output directory
cd compiled
rm *
cd ..

echo Compiling manual
xmlto -o compiled/ xhtml manual.xml

echo Compiling frame pages
xsltproc -o compiled/ xslt/frames.xsl manual.xml
