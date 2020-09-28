package org.acoli.glaser.metadata.pdf;

import java.net.URL;

public class SourceDescriptions {
    URL url;
    SourceTypes type;
    PDFExtractionConfiguration extractorConfig;

    public PDFExtractionConfiguration getExtractorConfig() {
        return extractorConfig;
    }

    boolean split;

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
