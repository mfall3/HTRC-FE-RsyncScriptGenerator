HTRC-FE-RsyncScriptGenerator
==========================

Reads in HathiTrust volume ids from the file identified in the properties file and generates a script to rsync Feature Extraction files for those volumes.

more information: https://sandbox.htrc.illinois.edu/HTRC-UI-Portal2/Features

Property file required values:

    collectionLocation=mylocation  the location of the list of volume ids
    outputDir=myOutputDir          the location of directory in which to put the output file
    outputFile=myOutputFile        the name of the output file