package org.acoli.glaser.metadata.pdf.extract;

import org.acoli.glaser.metadata.deprecatedClasses.SourceDescriptions;

import java.util.List;

public class Config {
    public String pathToTempDir;
    public List<SourceDescriptions> sources;

    public List<SourceDescriptions> getSources(){
        return sources;
    }
}