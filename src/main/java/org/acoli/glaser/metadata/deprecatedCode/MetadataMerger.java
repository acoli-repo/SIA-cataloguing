package org.acoli.glaser.metadata.deprecatedCode;

import org.acoli.glaser.metadata.units.util.Metadata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

/**
 * Can merge multiple Metadata objects. Currently merges ONLY on equal title. Probably more sophisticated stuff should
 * be added.
 */
public class MetadataMerger {

    private static Logger LOG = Logger.getLogger(Metadata.class.getName());

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
            LOG.warning("Conflicting entries a: "+a+", b:"+b);
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
            LOG.warning("Conflicting entries a: "+a+", b:"+b);
            return -1;
        }
        return -1;
    }
    static public List<String> mergeList(List<String> a, List<String> b) {
        if (a.size() == 0) {
            return b;
        } else if (b.size() == 0) {
            return a;
        }
        else {
            a.retainAll(b);
        }
        return a;
    }

    static public boolean areMergable(Metadata a, Metadata b) {
        return a.title.equals(b.title);
    }

    static public List<Metadata> mergeMetadata(List<Metadata> mds){
        List<Metadata> uniqMds = removeDuplicates(mds);
        if (uniqMds.size()==1) {
            return uniqMds;
        }
        List<Metadata> mergedMetadata = new ArrayList<>();
        for (int i = 0; i < uniqMds.size()-1; i++) {
            Metadata a = uniqMds.get(i);
            Metadata b = uniqMds.get(i+1);
            if (areMergable(a, b)) {
                mergedMetadata.add(mergeMetadata(a, b));
            } else {
                mergedMetadata.add(a);
                if (i == uniqMds.size()-2) {
                    mergedMetadata.add(b);
                }
            }
        }
        return mergedMetadata;
    }

    /**
     * This SHOULD have little impact on the performance.
     * @param mds
     * @return
     */
    static public List<Metadata> removeDuplicates(List<Metadata> mds){
        return new ArrayList<>(new HashSet<>(mds));
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
