
import org.acoli.glaser.metadata.pdf.MetadataSourceHandler;
import org.junit.Assert;
import org.junit.Test;

public class testSourceHandlers {


    public void testIfHandlerAdheresToBusinessLogic(MetadataSourceHandler msh) {
        Assert.assertFalse(msh.finished());
        msh.run();
        if (msh.finished()) {
            Assert.assertNotNull(msh.getMetadata());
        }
    }
}
