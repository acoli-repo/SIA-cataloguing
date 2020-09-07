import org.acoli.glaser.metadata.pdf.Metadata;
import org.acoli.glaser.metadata.pdf.MetadataMerger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class testMerging {

    private List<Metadata> twoMdsWithSameAuthorsAndTitle(){
        List<Metadata> testMds = new ArrayList<>();
        Metadata mdWithoutPages = new Metadata();
        mdWithoutPages.booktitle = "A book";
        mdWithoutPages.authors = new ArrayList<>(Arrays.asList("Alice A", "Bob B"));
        testMds.add(mdWithoutPages);
        testMds.add(mdWithoutPages);
        return testMds;
    }

    @Test
    public void mergeTwoIntoOne(){
        List<Metadata> mds = twoMdsWithSameAuthorsAndTitle();
        mds.get(0).beginPage = 1;
        mds.get(0).endPage = 42;
        MetadataMerger mm = new MetadataMerger();
        Metadata mergedMetadata = mm.mergeMetadata(mds.get(0),mds.get(1));
        Assert.assertEquals(mergedMetadata.title, mds.get(0).title);
        Assert.assertEquals(mergedMetadata.authors, mds.get(0).authors);
        Assert.assertEquals(mergedMetadata.beginPage, 1);
        Assert.assertEquals(mergedMetadata.endPage, 42);
    }
    @Test
    public void mergeTwoIntoTwo(){
        List<Metadata> mds = twoMdsWithSameAuthorsAndTitle();
        mds.get(0).title = "Not the same book!";
        MetadataMerger mm = new MetadataMerger();
        List<Metadata> mergedMetadata = mm.mergeMetadata(mds);
        Assert.assertNotEquals(mergedMetadata.get(0),mergedMetadata.get(1));
    }
    @Test
    public void mergeIdentity(){
        List<Metadata> mds = twoMdsWithSameAuthorsAndTitle();
        List<Metadata> mergedMds = MetadataMerger.mergeMetadata(mds);
        Assert.assertEquals(mergedMds.size(), 1);
        Assert.assertEquals(mds.get(0), mergedMds.get(0));
    }
    @Test
    public void mergeThreeIdentity(){
        List<Metadata> mds = twoMdsWithSameAuthorsAndTitle();
        mds.add(mds.get(0));
        List<Metadata> mergedMds = MetadataMerger.mergeMetadata(mds);
        Assert.assertEquals(mergedMds.size(), 1);
        Assert.assertEquals(mds.get(0), mergedMds.get(0));

    }
    @Test
    public void mergeThreeIntoThree(){
        List<Metadata> mds = twoMdsWithSameAuthorsAndTitle();
        mds.add(mds.get(0));
        mds.get(0).title = "Not the same book!";
        mds.get(1).title = "Also not the same book!";
        MetadataMerger mm = new MetadataMerger();
        List<Metadata> mergedMetadata = mm.mergeMetadata(mds);
        Assert.assertEquals(mergedMetadata.size(), 3);
        Assert.assertNotEquals(mergedMetadata.get(0),mergedMetadata.get(1));
        Assert.assertNotEquals(mergedMetadata.get(0),mergedMetadata.get(2));
        Assert.assertNotEquals(mergedMetadata.get(1),mergedMetadata.get(2));
    }

    @Test
    public void mergeTwoWithMissingAuthors(){
        List<Metadata> mds = twoMdsWithSameAuthorsAndTitle();
        mds.get(0).authors.clear();
        List<Metadata> mergedMds = MetadataMerger.mergeMetadata(mds);
        Assert.assertEquals(mergedMds.size(), 1);
        Assert.assertEquals(mergedMds.get(0).authors, mds.get(1).authors);

    }
}
