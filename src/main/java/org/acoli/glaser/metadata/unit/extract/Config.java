package org.acoli.glaser.metadata.unit.extract;

import org.acoli.glaser.metadata.deprecatedCode.SourceDescriptions;

import java.util.List;

public class Config {
    public String pathToTempDir;
    public List<SourceDescriptions> sources;

    public List<SourceDescriptions> getSources(){
        return sources;
    }
}