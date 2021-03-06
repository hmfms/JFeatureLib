/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lmu.ifi.dbs.jfeaturelib.utils;

import de.lmu.ifi.dbs.jfeaturelib.utils.Extractor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Franz
 */
public class ExtractorTest {

    private Path imgDir;
    private Path maskDir;
    private File imgFile;
    private File maskFile;

    public ExtractorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {
        imgDir = Files.createTempDirectory("junit_imgDir");
        maskDir = Files.createTempDirectory("junit_maskDir");

        // prepare image and mask file
        imgFile = new File(imgDir.toFile(), "image.jpg");
        imgFile.createNewFile();
        maskFile = new File(maskDir.toFile(), "image.png");
        maskFile.createNewFile();
    }

    @After
    public void tearDown() throws IOException {
        // delete directories recursively
        FileUtils.deleteDirectory(imgDir.toFile());
        FileUtils.deleteDirectory(maskDir.toFile());
    }

    @Test
    public void testFindTuples() throws IOException {
        Extractor extractor = new Extractor();
        extractor.maskDirectory = maskDir.toFile();
        extractor.imageDirectory = imgDir.toFile();

        Collection<File> imageList = extractor.createFileList(imgDir.toFile());
        assertEquals("image list must not be empty", 1, imageList.size());

        Collection<File> maskList = extractor.createFileList(maskDir.toFile());
        assertEquals("mask list must not be empty", 1, maskList.size());

        HashMap<File, File> tuples = extractor.findTuples(imageList, maskList);
        assertEquals("must contain 1 tuple", 1, tuples.size());
        assertTrue(tuples.keySet().iterator().next().toString().endsWith("image.jpg"));
        assertTrue(tuples.values().iterator().next().toString().endsWith("image.png"));
    }

    @Test
    public void testFindTuples2() throws IOException {
        // NOW start the actual test
        Extractor extractor = new Extractor();
        extractor.maskDirectory = maskDir.toFile();
        extractor.imageDirectory = imgDir.toFile();

        maskFile.delete();

        Collection<File> imageList = extractor.createFileList(imgDir.toFile());
        assertEquals("image list must not be empty", 1, imageList.size());

        Collection<File> maskList = extractor.createFileList(maskDir.toFile());
        assertEquals("mask list must be empty", 0, maskList.size());

        HashMap<File, File> tuples = extractor.findTuples(imageList, maskList);
        assertEquals("must contain 1 tuple", 1, tuples.size());
        assertTrue(tuples.keySet().iterator().next().toString().endsWith("image.jpg"));
        assertNull(tuples.values().iterator().next());
    }
}
