package org.acoli.sc.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import org.acoli.sc.extract.Metadata;

/**
 * unchanged
 * Can merge multiple Metadata objects. Currently merges ONLY on equal title. Probably more sophisticated stuff should
 * be added.
 */
public class MetadataMerger {

    private static Logger LOG = Logger.getLogger(Metadata.class.getName());

    static public String mergeString(String a, String b){
    	
    	a=a.trim();b=b.trim();
        if (!a.isEmpty() && b.isEmpty()){
            return a;
        }
        if (a.isEmpty() && !b.isEmpty()){
            return b;
        }
        if (!a.isEmpty() && a.equals(b)) {
            return a;
        }
        if (!a.isEmpty() && !b.isEmpty()) {
            LOG.warning("Conflicting entries a: "+a+", b:"+b);
            return a+b;
        }
        return "";
    }
    
    
    static int mergeInt(int a, int b) {
        if (a > 0 && b < 0)
            return a;
        if (a < 0 && b > 0)
            return b;
        if (a == b)
            return b;
        if (a > 0 && b>0 && a!=b) {
            LOG.warning("Conflicting entries a: "+a+", b:"+b);
            return -1;
        }
        return -1;
    }
    
    
    static public List<String> mergeList(List<String> a, List<String> b) {
    	    	
    	if (a.isEmpty() && !b.isEmpty()) return b;
    	if (!a.isEmpty() && b.isEmpty()) return a;
    	
    	// case when both lists are not empty not implemented
    	//if (!a.isEmpty() && !b.isEmpty())
    	
    	return new ArrayList<String>();

		/*
		 * if (a.isEmpty()) { return b; } else if (b.size() == 0) { return a; } else {
		 * a.retainAll(b); } return a;
		 */
    }

	/*
	 * static public boolean areMergable(Metadata a, Metadata b) { return
	 * a.title.equals(b.title); }
	 */

	
//	  static public List<Metadata> mergeMetadata(List<Metadata> mds){
//	  List<Metadata> uniqMds = removeDuplicates(mds); if (uniqMds.size()==1) {
//	  return uniqMds; } List<Metadata> mergedMetadata = new ArrayList<>(); for (int
//	  i = 0; i < uniqMds.size()-1; i++) { Metadata a = uniqMds.get(i); Metadata b =
//	  uniqMds.get(i+1); if (areMergable(a, b)) {
//	  mergedMetadata.add(mergeMetadata(a, b)); } else { mergedMetadata.add(a); if
//	  (i == uniqMds.size()-2) { mergedMetadata.add(b); } } } return mergedMetadata;
//	  }
	 

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
        c.journalTitle = mergeString(a.journalTitle, b.journalTitle);
        c.year = mergeInt(a.year, b.year);
        c.setUrl(mergeString(a.getUrl(), b.getUrl()));
        
        return c;
    }
    
    /**
     * Merge metadata by using Metadata.id
     * @param mergedmd
     * @param mdjson
     */
	public static List<Metadata> mergeMetadataNew(List<Metadata> mdextracted, List<Metadata> mdjson) {
		
		List<Metadata> merged = new ArrayList<Metadata>();
		HashMap<String, Metadata> mdm = new HashMap<String, Metadata>();
		for (Metadata md : mdjson) {
			//System.out.println("json ID "+md.getID());
			mdm.put(md.getFilenameWithoutExtension(), md);}
		
		for (Metadata md : mdextracted) {
			//System.out.println("extracted ID "+md.getID());

			if (mdm.containsKey(md.getFilenameWithoutExtension())) {
				//System.out.println("matched ID "+md.getID());
				merged.add(mergeMetadata(md, mdm.get(md.getFilenameWithoutExtension())));
			}
		}
		
		return merged;
	}
}
