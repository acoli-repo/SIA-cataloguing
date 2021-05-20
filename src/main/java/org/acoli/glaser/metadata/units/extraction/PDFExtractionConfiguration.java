package org.acoli.glaser.metadata.units.extraction;

public class PDFExtractionConfiguration {
    int authorHeight = -1;

    public PDFExtractionConfiguration(){

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

    public void setAuthorHeight(int authorHeight) {
        this.authorHeight = authorHeight;
    }

    public void setAuthorFont(int authorFont) {
        this.authorFont = authorFont;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }

    public void setTitleFont(int titleFont) {
        this.titleFont = titleFont;
    }

    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
    }

    public void setPageFont(int pageFont) {
        this.pageFont = pageFont;
    }

    int pageFont = -1;
}
