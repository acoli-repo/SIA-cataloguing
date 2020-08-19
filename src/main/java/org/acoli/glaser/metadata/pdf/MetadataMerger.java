package org.acoli.glaser.metadata.pdf;

import java.util.List;

public class MetadataMerger {


    static public String mergeString(String a, String b){
        if (a != "" && b == ""){
            return a;
        }
        if (a == "" && b != ""){
            return b;
        }
        if (a != "" && a.equals(b)) {
            return a;
        }
        if (a!= "" && b != "") {
            System.err.println("CONFLICTING ENTRIES");
            return a+b;
        }
        return "";
    }
    static int mergeInt(int a, int b) {
        if (a > 0 && b <= 0)
            return a;
        if (a <= 0 && b > 0)
            return b;
        if (a == b)
            return b;
        if (a >= 0 && b>0 && a!=b) {
            System.err.println("CONFLICTING ENTRIES");
            return -1;
        }
        return -1;
    }
    static public List<String> mergeList(List<String> a, List<String> b) {
        a.addAll(b);
        return a;
    }
    static public Metadata mergeMetadata(Metadata a, Metadata b){
        Metadata c = new Metadata();
        c.title = mergeString(a.title, b.title);
        c.authors = mergeList(a.authors, b.authors);
        c.beginPage = mergeInt(a.beginPage, b.beginPage);
        c.endPage = mergeInt(a.endPage, b.endPage);
        return c;
    }
}
