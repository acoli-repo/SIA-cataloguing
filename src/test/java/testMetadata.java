import org.acoli.glaser.metadata.pdf.extract.Metadata;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class testMetadata {

    @Test
    public void testEqualityTitle(){
        Metadata a = new Metadata();
        a.title = "A";
        Metadata b = new Metadata();
        b.title = "A";
        Assert.assertEquals(a, b);
    }
    @Test
    public void testEqualityAuthors(){
        Metadata a = new Metadata();
        a.authors = new ArrayList<>(Arrays.asList("Alice"));
        Metadata b = new Metadata();
        b.authors = new ArrayList<>(Arrays.asList("Bob"));
        Assert.assertNotEquals(a,b);
        b.authors = new ArrayList<>(Arrays.asList("Alice"));
        Assert.assertEquals(a,b);
        b.authors.add("Bob");
        Assert.assertNotEquals(a,b);
        a.authors.add("Bob");
        Assert.assertEquals(a,b);

    }
}
