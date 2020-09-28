package org.acoli.glaser.metadata.pdf;

public class PDFExtractionConfiguration {
    int authorHeight = -1;

    private PDFExtractionConfiguration(){

    }
    public static PDFExtractionConfiguration emptyConfig() {
        return new PDFExtractionConfiguration();
    }

    public int getAuthorHeight() {
        return authorHeight;
    }

    public int getAuthorFont() {
        return authorFont;
    }

    public int getTitleHeight() {
        return titleHeight;
    }

    public int getTitleFont() {
        return titleFont;
    }

    public int getPageHeight() {
        return pageHeight;
    }

    public int getPageFont() {
        return pageFont;
    }

    int authorFont = -1;
    int titleHeight = -1;
    int titleFont = -1;
    int pageHeight = -1;
    int pageFont = -1;
}
