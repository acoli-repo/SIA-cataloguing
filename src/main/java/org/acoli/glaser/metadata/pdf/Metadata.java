package org.acoli.glaser.metadata.pdf;

import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Metadata {
	public String fileName;
	public String title;
	public String journalTitle;
	public Integer year;
	public List<String> authors;
	public String location;
	public String booktitle;
	public int beginPage;
	public int endPage;


	public void setPageNumbers(List<Integer> pageNumbers) {
		if (pageNumbers.size()>1) {
			this.beginPage = pageNumbers.get(0);
			this.endPage = pageNumbers.get(pageNumbers.size() - 1);
		}
	}


	@Override
	public String toString() {
		StringBuilder repr = new StringBuilder();
		for (Object obj : Arrays.asList(title, String.join(", ", authors),booktitle,location, journalTitle, year, beginPage + "-" + endPage)) {
			repr.append(obj);
			repr.append("\n\t");
		}
		return repr.toString();
	}

	public boolean hasTitle() {
		return  title != null;
	}
	public boolean hasAuthors() {
		return authors != null;
	}

	/**
	 * TODO: Implement this
	 * @param bibtex
	 * @return
	 */
	@Deprecated
	public static Metadata metadataFromBibtex(String bibtex) {
		final Pattern GET_TYPE = Pattern.compile("^@(.*?)\\{");
		final Pattern GET_TITLE = Pattern.compile("title = \\{(.*?)}");
		final Pattern GET_AUTHORS = Pattern.compile("author = \\{(.*?)}");
		String authors = "";
		String title = "";
		int beginPage = -1;
		int endPage = -1;
		Matcher m = GET_TYPE.matcher(bibtex);
		if (m.find()) {
			System.err.println("Found type: "+m.group(1));
		}
		m = GET_AUTHORS.matcher(bibtex);
		if (m.find()) {
			authors = m.group(1);
			System.err.println("Found author: "+authors);
		}
		m = GET_TITLE.matcher(bibtex);
		if (m.find()) {
			title = m.group(1);
			System.err.println("Found title: "+title);
		}
		Metadata md = new Metadata();
		md.authors = new ArrayList<>(Arrays.asList(authors.split(",|and")));
		md.title = title;
		return md;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}
