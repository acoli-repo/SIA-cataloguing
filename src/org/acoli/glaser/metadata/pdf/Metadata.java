package org.acoli.glaser.metadata.pdf;

import java.util.List;

public class Metadata {
	String fileName;
	String title;
	List<String> authors;
	int beginPage;
	int endPage;


	public void setPageNumbers(List<Integer> pageNumbers) {
		if (pageNumbers.size()>1) {
			this.beginPage = pageNumbers.get(0);
			this.endPage = pageNumbers.get(pageNumbers.size() - 1);
		}
	}

	@Override
	public String toString() {
		return this.title + "\n\t" + String.join(", ", this.authors) + "\n\t" + this.beginPage + "-" + this.endPage;
	}
}
