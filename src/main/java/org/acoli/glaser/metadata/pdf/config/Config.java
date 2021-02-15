package org.acoli.glaser.metadata.pdf.config;

import java.util.List;

public class Config {
    public String pathToTempDir;
    public List<SourceDescriptions> sources;

    public List<SourceDescriptions> getSources(){
        return sources;
    }
}