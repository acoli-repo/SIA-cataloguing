import org.acoli.glaser.metadata.pdf.extract.Metadata;
import org.acoli.glaser.metadata.pdf.util.MetadataMerger;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class testMerging {

    private Metadata copy(Metadata md){
        Metadata o = new Metadata();
        o.title = md.title;
        o.authors = new ArrayList<>(md.authors);
        o.endPage = md.endPage;
        o.beginPage = md.beginPage;
        return o;
    }

    private List<Metadata> twoMdsWithSameAuthorsAndTitle(){
        List<Metadata> testMds = makeKEmptyMetadata(1);
        testMds.get(0).title = "A book";
        testMds.get(0).authors = new ArrayList<>();
        testMds.get(0).authors.add("Alice A.");
        testMds.get(0).authors.add("Bob B.");
        testMds.add(copy(testMds.get(0)));
        return testMds;
    }
    @Test
    public void testTestHelperMethods(){
        List<Metadata> test = twoMdsWithSameAuthorsAndTitle();
        Assert.assertEquals(2, test.size());
        Assert.assertEquals(2, test.get(0).authors.size());
        Assert.assertEquals(2, test.get(1).authors.size());

    }
    private List<Metadata> makeKEmptyMetadata(int k){
        List<Metadata> result = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            result.add(new Metadata());
        }
        return result;
    }
    @Test
    public void testMakeKEmptyMetadata(){
        Assert.assertEquals(makeKEmptyMetadata(1).size(), 1);
        Assert.assertEquals(makeKEmptyMetadata(4).size(), 4);
        Assert.assertEquals(makeKEmptyMetadata(5).size(), 5);
    }
    @Test
    public void ensureCopyChanges(){
        Metadata md = new Metadata();
        md.title = "Original";
        md.authors = new ArrayList<>();
        md.beginPage = 1;
        md.endPage = 2;
        Metadata other = copy(md);
        other.title = "Copy";
        other.authors.add("Copy");
        md.beginPage++;
        md.endPage++;
        Assert.assertNotEquals(md.title, other.title);
        Assert.assertNotEquals(md.authors, other.authors);
        Assert.assertNotEquals(md.beginPage, other.beginPage);
        Assert.assertNotEquals(md.endPage, other.endPage);
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
        System.err.println(mds);
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
        mds.add(copy(mds.get(0)));
        mds.get(0).title = "Not the same book!";
        mds.get(1).title = "Also not the same book!";
        System.err.println(mds);
        MetadataMerger mm = new MetadataMerger();
        List<Metadata> mergedMetadata = mm.mergeMetadata(mds);
        System.err.println(mergedMetadata);
        Assert.assertEquals(mergedMetadata.size(), 3);
        Assert.assertNotEquals(mergedMetadata.get(0),mergedMetadata.get(1));
        Assert.assertNotEquals(mergedMetadata.get(0),mergedMetadata.get(2));
        Assert.assertNotEquals(mergedMetadata.get(1),mergedMetadata.get(2));
    }

    @Test
    public void mergeTwoWithMissingAuthors(){
        List<Metadata> mds = twoMdsWithSameAuthorsAndTitle();
        mds.get(0).authors.clear();
        System.err.println(mds);
        List<Metadata> mergedMds = MetadataMerger.mergeMetadata(mds);
        Assert.assertEquals(mergedMds.size(), 1);
        System.err.println(mergedMds);
        Assert.assertEquals(mergedMds.get(0).authors, mds.get(1).authors);

    }
    @Test
    public void mergeThreeIntoOneTransitive(){
        List<Metadata> mds = makeKEmptyMetadata(3);

    }
}
