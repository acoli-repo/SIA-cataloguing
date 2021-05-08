package org.acoli.glaser.metadata.deprecatedCode;

import org.acoli.glaser.metadata.unit.extract.PDFExtractionConfiguration;

import java.net.URL;

public class SourceDescriptions {
    public URL url;
    public SourceTypes type;
    public PDFExtractionConfiguration extractorConfig;

    public PDFExtractionConfiguration getExtractorConfig() {
        return extractorConfig;
    }

    public boolean split;

    @Override
    public String toString() {
        return "SourceDescriptions{" +
                url +
                "(" + type +
                ")}";
    }

    public URL getUrl() {
        return url;
    }

    public SourceTypes getType() {
        return type;
    }
}
