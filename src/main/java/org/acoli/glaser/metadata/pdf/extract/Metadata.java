package org.acoli.glaser.metadata.pdf.extract;

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
	public int volume;
	public String issue;
	public String journalNote;


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


	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public int getExtent(){
		return endPage- beginPage;
	}

	public String getPages(){
		int pages = getExtent();
		return String.valueOf(pages);
	}
}
