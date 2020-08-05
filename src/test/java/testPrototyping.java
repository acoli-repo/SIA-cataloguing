import org.acoli.glaser.metadata.pdf.FileHandler;
import org.acoli.glaser.metadata.pdf.MetadataFromHTML;
import org.acoli.glaser.metadata.pdf.Source;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import java.io.*;

public class testPrototyping {

    @Test
    public void readUrlSeed() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(new File("urlseed.csv")));

        for(String line = in.readLine(); line!=null; line=in.readLine()) {
            System.err.println(line);
            Source s = Source.sourceFromCSVRow(line);
            if (s.urlAsString.startsWith("http")) {
                System.err.println("Downloading "+s+"..");
                Document doc = Jsoup.connect(s.urlAsString).get();
                Elements paperPapers = doc.select(".paper_papers > a");
                System.err.println(paperPapers);
                for (Element e : paperPapers) {
                    String urlToSummary = e.absUrl("href");
                    System.err.println("Following url to summary "+urlToSummary+"..");
                    Document singlePage = Jsoup.connect(e.absUrl("href")).get();
//                    MetadataFromHTML.parseSummaryPage(singlePage);
                    //  <table class="main_summaries">
                }
            }
        }
    }

    @Test
    public void testMetadataFromHTMLWithSingleLink() {
        String testURL = "http://lrec-conf.org/workshops/lrec2018/W29/summaries/6_W29.html";
        MetadataFromHTML mfh = new MetadataFromHTML(testURL, new FileHandler());
        mfh.parseSummaryPage();
        mfh.findPDFofPublicationAndDownload();
    }
}
