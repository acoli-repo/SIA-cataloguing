package org.acoli.glaser.metadata.unit.extract;

import org.acoli.glaser.metadata.deprecatedCode.SourceDescriptions;

import java.util.List;

public class Config {
    public String PathToTempDir;
    public String DocumentRootDir;
    public List<SourceDescriptions> sources;

    public List<SourceDescriptions> getSources(){
        return sources;
    }
}