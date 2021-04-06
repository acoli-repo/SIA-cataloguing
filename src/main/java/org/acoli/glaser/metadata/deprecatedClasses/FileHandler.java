package org.acoli.glaser.metadata.deprecatedClasses;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Handles concerns about file handling like downloading, saving, returning them.
 */
public class FileHandler {
    File rootDir;

    private static Logger LOG = Logger.getLogger(FileHandler.class.getName());

    public FileHandler() {
        rootDir = new File("tempDir");
        LOG.info("Initialized FileHandler at "+rootDir.getAbsolutePath());
    }

    public File downloadFileToTemp(URL url) throws IOException {
        File targetFile = Paths.get(rootDir.getAbsolutePath(),String.valueOf(url.hashCode())).toFile();
        LOG.info("Saving "+url+" to "+targetFile.getAbsolutePath()+"..");
        FileUtils.copyURLToFile(url, targetFile);
        return targetFile;
    }
    public File downloadFileToTemp(String url) throws IOException {
        return downloadFileToTemp(new URL(url));
    }

    public File getRootDir() {
        return this.rootDir;
    }

    /**
     * Produces an unique subdir in the tempfolder that a MetadataObject can write temp
     * files into
     * @return
     */
    public File getUniqueSubfolderInTempDirForHandler() {
        // TODO: implement
        return null;
    }
    /**
     * @param maybeAnURL
     * @return
     */
    public static boolean isURL(String maybeAnURL) {
        try {
            new URL(maybeAnURL);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
