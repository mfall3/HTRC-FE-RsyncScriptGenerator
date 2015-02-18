package main;

import java.io.*;
import java.util.Properties;
import util.PropertyManager;
import java.nio.channels.FileChannel;

/**
 * HTRC FE Rsync Script Generator
 *
 * <P>Pre-requisite:  a collection.properties file</p>
 * collectionLocation=mylocation  -- the location of the list of volume ids
 * outputDir=myOutputDir -- the location of directory in which to put the output file
 * outputFile=myOutputFile  -- the name of the output file
 *
 */
public class main
{
    public static void main(String[] args)
            throws Exception
    {
        Properties properties = new Properties();
        properties.load(new FileInputStream("collection.properties"));
        String location = properties.getProperty("collectionLocation");
        String OutputDir_path = PropertyManager.getProperty("outputDir");
        String result_name = PropertyManager.getProperty("outputFile");

        PrintWriter printWriter = new PrintWriter(result_name);
        printWriter.write("#!/bin/bash\n");

        BufferedReader br = new BufferedReader(new FileReader(location));
        br.readLine();//throw away the fist line, which is a header row

        String volumeId;
        while ((volumeId = br.readLine()) != null) {
            String sourcePart = volumeId.substring(0, volumeId.indexOf("."));
            String volumePart = volumeId.substring(volumeId.indexOf(".") + 1, volumeId.length());
            String cleanVolumePart = volumePart.replace(':', '+');
            cleanVolumePart = cleanVolumePart.replace('/', '=');
            printWriter.write("rsync -v sandbox.htrc.illinois.edu::ngpd-features/" + sourcePart + '/' + sourcePart + "." + cleanVolumePart  + ".json.bz2" + " $(pwd)" + "\n");
        }

        printWriter.close();

        File final_result = new File(OutputDir_path, result_name);
        if (final_result.exists()) {
            final_result.delete();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(new File(result_name)).getChannel();
            destination = new FileOutputStream(final_result).getChannel();
            destination.transferFrom(source, 0, source.size());
        }
        finally {
            if(source != null) {
                source.close();
            }
            if(destination != null) {
                destination.close();
            }
        }

    }

    /**
     *
     * @param volId     HathiTrust volume id
     * @return          Rsync Command to retrieve given volume id
     */
    String getRsyncCommandFromVolumeID (String volId)  {
        String volumeId = volId;
        String sourcePart = volumeId.substring(0, volumeId.indexOf("."));
        String volumePart = volumeId.substring(volumeId.indexOf(".") + 1, volumeId.length());
        String cleanVolumePart = volumePart.replace(':', '+');
        cleanVolumePart = cleanVolumePart.replace('/', '=');
        String rsyncCommandString = "rsync -v sandbox.htrc.illinois.edu::ngpd-features/" + sourcePart + '/' + sourcePart + "." + cleanVolumePart  + ".json.bz2" + " $(pwd)";
        return rsyncCommandString;
    }

}