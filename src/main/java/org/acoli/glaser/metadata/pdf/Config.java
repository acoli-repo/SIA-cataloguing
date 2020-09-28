package org.acoli.glaser.metadata.pdf;

import java.util.List;

public class Config {
    String pathToTempDir;
    List<SourceDescriptions> sources;

    public List<SourceDescriptions> getSources(){
        return sources;
    }
}
