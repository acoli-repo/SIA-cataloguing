package org.acoli.sc.extract;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.acoli.sc.config.Config;
import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

/*
 * unchanged
 */
public class PDFExtractionConfiguration {
	
	private static Logger LOG = Logger.getLogger(PDFExtractionConfiguration.class.getName());

	int titleFont = -1;
	int authorFont = -1;
	int pageFont = -1;
	int footerFont = -1;

    private int titleHeight = -1;
    private int titleHeightMin = -1;
    private int titleHeightMax = -1;
    private int titleOrdering = -1;

    private int authorHeight = -1;
    private int authorHeightMin = -1;
    private int authorHeightMax = -1;

    private int pageHeight = -1;
    private int pageHeightMin = -1;
    private int pageHeightMax = -1;
    
    private int footerHeight = -1;
    private int footerHeightMin = -1;
    private int footerHeightMax = -1;
    private int footerPage = -1;


    String titleHeightRelation = "=";
	String authorHeightRelation = "=";
    String pageHeightRelation = "=";
    String footerHeightRelation = "=";

    
    String authorFontRelation = "=";
    String titleFontRelation = "=";
    String pageFontRelation = "=";
    String footerFontRelation = "=";
    
    //String footerDescription="";
    
    private int authorTitleTop = -1;
    private String authorTitletopRelation = "<=";
    
    private int footerTop = -1;
    private String footerTopRelation = "<=";
	private String footerStartsWithText="";
	
	private List<Footer> footerDescriptions = new ArrayList<Footer>();

    
    int authorMinLength = 4;
    int authorMaxLength = -1;
    
    boolean authorAllUppercase = false;
    boolean authorNoDigits = true;
    
    int firstPage=-1;
    String comment="";
    
    String bindSequentialLinesBy = "fontType"; // allowed : font | fontSize | boldFont | italicFont
    String bindSequentialLinesOperator = "or"; // or | and
    
    String bindLineGroupsBy = "fontType+fontSize"; // allowed : font | fontSize | boldFont | italicFont
    String bindLineGroupsOperator = "and"; // or | and
    
    //boolean toc=false;
    
    boolean titleFontBold = false;
	boolean authorFontBold = false;
	boolean pageFontBold = false;
    
	boolean titleFontItalic = false;
	boolean authorFontItalic = false;
	boolean pageFontItalic = false;
	
	private List<TOC> tocDescriptions = null;
	
	private List<Citation> citationDescriptions = null;
	



	public PDFExtractionConfiguration(){}
    
    public static PDFExtractionConfiguration emptyConfig() {
        return new PDFExtractionConfiguration();
    }

    
    public void setAuthorFont(int authorFont) {
        this.authorFont = authorFont;
    }
    
    public int getAuthorFont() {
        return authorFont;
    }

    public void setTitleFont(int titleFont) {
        this.titleFont = titleFont;
    }
    
    public int getTitleFont() {
        return titleFont;
    }

    public void setPageFont(int pageFont) {
        this.pageFont = pageFont;
    }
    
    public int getPageFont() {
        return pageFont;
    }

    /**
     * Set height of author font (disables min/max setting)
     * @param authorHeight
     */
    public void setAuthorHeight(int authorHeight) {
        this.authorHeight = authorHeight;
        this.authorHeightMin = authorHeight;
        this.authorHeightMax = authorHeight;
    }
    
    public int getAuthorHeight() {
        return authorHeight;
    }
    
    /**
	 * Set min. and max. author height (disables author height)
     * @param authorHeightMin
     * @param authorHeightMax
     */
    public void setAuthorHeightMinMax(int authorHeightMin, int authorHeightMax) {
    	if (authorHeightMin > authorHeightMax) {
			System.out.println("Error in setAuthorHeightMinMax "+authorHeightMin+" > "+authorHeightMax +" invalid !");
			return;
		}
		this.authorHeightMin = authorHeightMin;
		this.authorHeightMax = authorHeightMax;
		this.authorHeight = -1;
	}
    
    public int getAuthorHeightMin() {
        return authorHeightMin;
    }
    
    public int getAuthorHeightMax() {
        return authorHeightMax;
    }

    /**
     * Set height of title font (disables min/max setting)
     * @param titleHeight
     */
    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
        this.titleHeightMin = titleHeight;
        this.titleHeightMax = titleHeight;
    }
    
    public int getTitleHeight() {
        return titleHeight;
    }
    
    /**
	 * Set min. and max. title height (disables title height)
	 * @param titleHeightMin
	 * @param titleHeightMax
	 */
	public void setTitleHeightMinMax(int titleHeightMin, int titleHeightMax) {
		if (titleHeightMin > titleHeightMax) {
			System.out.println("Error in setTitleHeightMinMax "+titleHeightMin+" > "+titleHeightMax+" invalid !");
			return;
		}
		this.titleHeightMin = titleHeightMin;
		this.titleHeightMax = titleHeightMax;
		this.titleHeight = -1;
	}

	public int getTitleHeightMin() {
		return titleHeightMin;
	}
	
	public int getTitleHeightMax() {
		return titleHeightMax;
	}
    
    
    /**
     * Set page height (disables min/max setting)
     * @param pageHeight
     */
    public void setPageHeight(int pageHeight) {
        this.pageHeight = pageHeight;
        this.pageHeightMin = -1;
        this.pageHeightMax = -1;
    }
    
    public int getPageHeight() {
        return pageHeight;
    }
    
    /**
 	 * Set min. and max. page height (disables page height)
     * @param pageHeightMin
     * @param pageHeightMax
     */
    public void setPageHeightMinMax(int pageHeightMin, int pageHeightMax) {
    	if (pageHeightMin > pageHeightMax) {
			System.out.println("Error in setPageHeightMinMax "+pageHeightMin+" > "+pageHeightMax+" invalid !");
			return;
		}
		this.pageHeightMin = pageHeightMin;
		this.pageHeightMax = pageHeightMax;
		this.pageHeight = -1;
	}
    
    public int getPageHeightMin() {
        return pageHeightMin;
    }
   
    public int getPageHeightMax() {
        return pageHeightMax;
    }
    
    
	public String getAuthorHeightRelation() {
		return authorHeightRelation;
	}

	public void setAuthorHeightRelation(String authorRelation) {
		this.authorHeightRelation = authorRelation;
	}

	public String getPageHeightRelation() {
		return pageHeightRelation;
	}

	public void setPageHeightRelation(String pageRelation) {
		this.pageHeightRelation = pageRelation;
	}

	public String getTitleHeightRelation() {
		return titleHeightRelation;
	}

	public void setTitleHeightRelation(String titleRelation) {
		this.titleHeightRelation = titleRelation;
	}
	
	public String getAuthorFontRelation() {
		return authorFontRelation;
	}

	public void setAuthorFontRelation(String authorFontRelation) {
		this.authorFontRelation = authorFontRelation;
	}
	
	public boolean authorFontIsActive() {
		return authorFont >= 0;
	}
	
	public boolean firstPageIsActive() {
		return firstPage > 0;
	}
	
	public boolean titleFontIsActive() {
		return titleFont >= 0;
	}
	
	public boolean titleHeightIsActive() {
		return titleHeight >= 0;
	}
	
	public boolean pageHeightIsActive() {
		return pageHeight >= 0;
	}
	
	public boolean authorHeightIsActive() {
		return authorHeight >= 0;
	}
	
	public boolean authorMinLengthIsActive() {
		return authorMinLength >= 0;
	}
	
	public boolean authorMaxLengthIsActive() {
		return authorMaxLength >= 0;
	}

	public boolean isAuthorAllUppercase() {
		return authorAllUppercase;
	}

	public void setAuthorAllUppercase(boolean authorAllUppercase) {
		this.authorAllUppercase = authorAllUppercase;
	}

	public boolean isAuthorNoDigits() {
		return authorNoDigits;
	}

	public void setAuthorNoDigits(boolean authorNoDigits) {
		this.authorNoDigits = authorNoDigits;
	}

	public int getAuthorMinLength() {
		return authorMinLength;
	}

	public void setAuthorMinLength(int authorMinLength) {
		this.authorMinLength = authorMinLength;
	}

	public int getAuthorMaxLength() {
		return authorMaxLength;
	}

	public void setAuthorMaxLength(int authorMaxLength) {
		this.authorMaxLength = authorMaxLength;
	}
	
	
	public Boolean compareTitleHeight(int value) {
		return compareByRelation(titleHeightRelation, value);
	}
	
	public Boolean compareAuthorHeight(int value) {
		return compareByRelation(authorHeightRelation, value);
	}
	
	public Boolean comparePageHeight(int value) {
		return compareByRelation(pageHeightRelation, value);
	}
	
	private Boolean compareByRelation(String relation, int value) {
		
		switch(relation) {

		case "=":
			return (titleHeight == value);
		case "<":
			return (titleHeight == value);
		case ">":
			return (titleHeight == value);
		case ">=":
			return (titleHeight == value);
		case "<=":
			return (titleHeight == value);
		
		default:
			System.out.println("Error compareByRelation : operator "+
					relation+" not recognized !");
			
			return null;
		}
	}

	
	
	static String readConfigJSONToString(String path) {
        try {
            return FileUtils.readFileToString(new File(path));
        } catch (IOException e) {
            LOG.info("Couldn't read JSON config file.");
        }
        return null;
    }
    
    static public Config readConfigs(String pathToConfigFile) {
        Gson gson = new Gson();
        String json = readConfigJSONToString(pathToConfigFile);
        Config config = gson.fromJson(json, Config.class);
        return config;
    }

	public Integer getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(Integer page) {
		this.firstPage = page;
	}

	public String getTitleFontRelation() {
		return titleFontRelation;
	}

	public void setTitleFontRelation(String titleFontRelation) {
		this.titleFontRelation = titleFontRelation;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getFooterFont() {
		return footerFont;
	}

	public void setFooterFont(int footerFont) {
		this.footerFont = footerFont;
	}
	
	public boolean footerFontIsActive() {
		return footerFont >= 0;
	}

	public int getFooterHeight() {
		return footerHeight;
	}

	public void setFooterHeight(int footerHeight) {
		this.footerHeight = footerHeight;
	}

	public int getFooterHeightMin() {
		return footerHeightMin;
	}

	public void setFooterHeightMin(int footerHeightMin) {
		this.footerHeightMin = footerHeightMin;
	}

	public int getFooterHeightMax() {
		return footerHeightMax;
	}

	public void setFootereightMax(int footerHeightMax) {
		this.footerHeightMax = footerHeightMax;
	}
	
	public boolean footerHeightIsActive() {
		return footerHeight >= 0;
	}

	public String getFooterFontRelation() {
		return footerFontRelation;
	}

	public void setFooterFontRelation(String footerFontRelation) {
		this.footerFontRelation = footerFontRelation;
	}

	public String getFooterHeightRelation() {
		return footerHeightRelation;
	}

	public void setFooterHeightRelation(String footerHeightRelation) {
		this.footerHeightRelation = footerHeightRelation;
	}

	public int getFooterPage() {
		return footerPage;
	}

	public void setFooterPage(int footerPage) {
		this.footerPage = footerPage;
	}
	
	public boolean footerPageIsActive() {
		return footerPage > 0;
	}

//	public String getFooterDescription() {
//		return footerDescription;
//	}
//
//	public void setFooterDescription(String footerDescription) {
//		this.footerDescription = footerDescription;
//	}
//	
//	public boolean footerDescriptionIsActive() {
//		return !footerDescription.trim().isEmpty();
//	}

	public int getAuthorTitleTop() {
		return authorTitleTop;
	}

	public void setAuthorTitleTop(int top) {
		this.authorTitleTop = top;
	}

	public String getAuthorTitleTopRelation() {
		return authorTitletopRelation;
	}

	public void setAuthorTitleTopRelation(String topRelation) {
		this.authorTitletopRelation = topRelation;
	}
	
	public boolean authorTitleTopIsActive() {
		return authorTitleTop > 0;
	}

	public int getFooterTop() {
		return footerTop;
	}

	public String getFooterTopRelation() {
		return footerTopRelation;
	}

	public void setFooterTopRelation(String footerTopRelation) {
		this.footerTopRelation = footerTopRelation;
	}

	public void setFooterTop(int footerTop) {
		this.footerTop = footerTop;
	}
	
	public boolean footerTopIsActive() {
		return footerTop > 0;
	}

	public boolean footerStartsWithTextIsActive() {
		return !footerStartsWithText.trim().isEmpty();
	}

	public String getFooterStartsWithText() {
		return footerStartsWithText;
	}

	public void setFooterStartsWithText(String text) {
		this.footerStartsWithText = text;
	}

	public int getTitleOrdering() {
		return titleOrdering;
	}

	public void setTitleOrdering(int titleOrdering) {
		this.titleOrdering = titleOrdering;
	}
	
	public boolean titleOrderingIsActive() {
		return titleOrdering > 0;
	}

	public String getBindSequentialLinesBy() {
		return bindSequentialLinesBy;
	}

	public void setBindSequentialLinesBy(String bindSequentialLinesBy) {
		this.bindSequentialLinesBy = bindSequentialLinesBy;
	}
	
	public boolean bindSequentialLinesByFontIsActive() {
		return bindSequentialLinesBy.toLowerCase().contains("fonttype");
	}
	
	public boolean bindSequentialLinesByFontSizeIsActive() {
		return bindSequentialLinesBy.toLowerCase().contains("fontsize");
	}
	
	public boolean bindSequentialLinesByBoldFontIsActive() {
		return bindSequentialLinesBy.toLowerCase().contains("boldfont");
	}
	
	public boolean bindSequentialLinesByItalicFontIsActive() {
		return bindSequentialLinesBy.toLowerCase().contains("italicfont");
	}
	
	public boolean bindLineGroupsByFontIsActive() { 
		return bindLineGroupsBy.toLowerCase().contains("fonttype");
	}
	
	public boolean bindLineGroupsByFontSizeIsActive() {
		return bindLineGroupsBy.toLowerCase().contains("fontsize");
	}
	
	public boolean bindLineGroupsByBoldFontIsActive() {
		return bindLineGroupsBy.toLowerCase().contains("boldfont");
	}
	
	public boolean bindLineGroupsByItalicFontIsActive() {
		return bindLineGroupsBy.toLowerCase().contains("italicfont");
	}

	public List<Footer> getFooterDescriptions() {
		return footerDescriptions;
	}

	public void setFooterDescriptions(List<Footer> footerDescriptions) {
		this.footerDescriptions = footerDescriptions;
	}

	public String getBindSequentialLinesOperator() {
		return bindSequentialLinesOperator;
	}

	public void setBindSequentialLinesOperator(String bindSequentialLinesOperator) {
		this.bindSequentialLinesOperator = bindSequentialLinesOperator;
	}

	public String getBindLineGroupsBy() {
		return bindLineGroupsBy;
	}

	public void setBindLineGroupsBy(String bindLineGroupsBy) {
		this.bindLineGroupsBy = bindLineGroupsBy;
	}

	public String getBindLineGroupsOperator() {
		return bindLineGroupsOperator;
	}

	public void setBindLineGroupsOperator(String bindLineGroupsOperator) {
		this.bindLineGroupsOperator = bindLineGroupsOperator;
	}
	
	public boolean hasToc() {
			return (tocDescriptions != null && !tocDescriptions.isEmpty());
	}
	
	public boolean hasCitation() {
		return (citationDescriptions != null && !citationDescriptions.isEmpty());
	}
	
	public boolean hasFooter() {
		return (footerDescriptions != null && !footerDescriptions.isEmpty());
	}

//	public void setToc(boolean toc) {
//		this.toc = toc;
//	}

 	public boolean isTitleFontBold() {
		return titleFontBold;
	}

	public void setTitleFontBold(boolean titleFontBold) {
		this.titleFontBold = titleFontBold;
	}

    public boolean isAuthorFontBold() {
		return authorFontBold;
	}

	public void setAuthorFontBold(boolean authorFontBold) {
		this.authorFontBold = authorFontBold;
	}

    public boolean isPageFontBold() {
		return pageFontBold;
	}

	public void setPageFontBold(boolean pageFontBold) {
		this.pageFontBold = pageFontBold;
	}

    public boolean isTitleFontItalic() {
		return titleFontItalic;
	}

	public void setTitleFontItalic(boolean titleFontItalic) {
		this.titleFontItalic = titleFontItalic;
	}

    public boolean isAuthorFontItalic() {
		return authorFontItalic;
	}

	public void setAuthorFontItalic(boolean authorFontItalic) {
		this.authorFontItalic = authorFontItalic;
	}

	public boolean isPageFontItalic() {
		return pageFontItalic;
	}

	public void setPageFontItalic(boolean pageFontItalic) {
		this.pageFontItalic = pageFontItalic;
		}

	public List<TOC> getTocDescriptions() {
		return tocDescriptions;
	}

	public void setTocDescriptions(List<TOC> tocDescription) {
		this.tocDescriptions = tocDescription;
	}

	public List<Citation> getCitationDescriptions() {
		return citationDescriptions;
	}

	public void setCitationDescriptions(List<Citation> citationDescriptions) {
		this.citationDescriptions = citationDescriptions;
	}

}
