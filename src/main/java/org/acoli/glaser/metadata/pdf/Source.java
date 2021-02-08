package org.acoli.glaser.metadata.pdf;


/**
 * Defines a single source of data
 */
@Deprecated
public class Source {
    public String name;
    public String urlAsString;
    public SourceDescriptions sds;


    @Deprecated
    public Source(String name, String urlAsString, boolean pdfContainMultiple) {
        this.name = name;
        this.urlAsString = urlAsString;
    }


    public static Source sourceFromCSVRow(String row) {
        String[] split = row.split(";");
        if (split.length != 3) {
            System.err.println("NOT length 3 "+row);
        }

        return new Source(split[0],split[1], split[2].trim().toLowerCase().equals("true"));
    }
}