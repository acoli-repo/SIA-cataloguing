package org.acoli.glaser.metadata.pdf;


import java.util.List;

public class Metadata2TTL {

	public String metadataToTTL(Metadata md) {
		StringBuilder ttl = new StringBuilder();
		ttl.append(makeHeader(md.fileName));
		ttl.append("<"+makeIdentifier(md)+"> "+"dcterms:title \""+md.title+"\"@eng ;");
		ttl.append("\n");
		ttl.append("bibo:pages \""+md.beginPage+"-"+md.endPage+"\" ;\n");
		ttl.append("bibo:authorList <"+makeAuthorListIdentifier(md)+"> .\n");
		ttl.append("\n");
		ttl.append(makeAuthorList(md));


		return ttl.toString();
	}

	public String makeAuthorListIdentifier(Metadata md) {
		return makeIdentifier(md)+"#authorList";
	}
	public String makeAuthorList(Metadata md) {
		String authorList = "<"+makeAuthorListIdentifier(md)+"> a rdf:Seq ;\n";
		for (int i = 0; i < md.authors.size(); i++) {
			authorList += "rdf:_"+(i+1)+" \""+md.authors.get(i)+"\" ";
			if (i == md.authors.size() - 1) {
				authorList += ".\n";
			} else {
				authorList += ";\n";
			}
		}
		return authorList;

	}

	public String makeIdentifier(Metadata md) {
		return "http://example.com/LREC/articles/"+md.fileName+"";
	}
	public String makeHeader(String identifier) {
		return  "@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .\n"+
				"@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .\n"+
				"@prefix bibo: <http://purl.org/ontology/bibo/> .\n"+
				"@prefix dcterms: <http://purl.org/dc/terms/> .\n";//+
//				"@prefix : <"+identifier+">\n";
	}
}
