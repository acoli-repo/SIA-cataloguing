package org.acoli.sc.extract;

import java.util.Objects;

public class Author {
	
	private String rawName="";
	private String givenName = "";
	private String familyName = "";
	private boolean primaryAuthor = false;
	
	
	public Author(String given, String family) {
		this.givenName = given;
		this.familyName = family;
	}
	
	public Author() {}
	
	public String getRawName() {
		return rawName;
	}
	public void setRawName(String rawName) {
		this.rawName = rawName;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	@Override
	public String toString() {
		
		String string = "given : "+givenName+"\n";
		string += "family : "+familyName+"\n";
		return string;
	}
	@Override
	public int hashCode() {
		return Objects.hash(familyName, givenName);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Author other = (Author) obj;
		return Objects.equals(familyName, other.familyName) && Objects.equals(givenName, other.givenName);
	}
	public boolean isPrimaryAuthor() {
		return primaryAuthor;
	}
	public void setPrimaryAuthor(boolean primaryAuthor) {
		this.primaryAuthor = primaryAuthor;
	}
	
	
	
	
}
