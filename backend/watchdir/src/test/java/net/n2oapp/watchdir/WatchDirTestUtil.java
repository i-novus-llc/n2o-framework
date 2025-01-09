package net.n2oapp.watchdir;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author iryabov
 * @since 08.12.2015
 */
public class WatchDirTestUtil {
    public static final String TEST_DIR = getTestFolder();
    public static final String TEST_FILE = TEST_DIR + "test.txt";
    public static final String TEST2_FILE = TEST_DIR + "test2.txt";
    public static final String SUB_DIR = TEST_DIR + "dir" + File.separator;
    public static final String SUB2_DIR = SUB_DIR + "sub" + File.separator;
    public static final String SUB2_FILE1 = SUB2_DIR + "file1.txt";
    public static final String SUB2_FILE2 = SUB2_DIR + "file2.txt";

    private static String getTestFolder() {
        return System.getProperty("user.home") +
                File.separator +
                WatchDirTest.class.getSimpleName() +
                File.separator;
    }

    public static void createTestDir() throws IOException {
        clearTestDir();
        assertTrue(new File(TEST_DIR).mkdirs());
    }

    public static void clearTestDir() throws IOException {
        File testDir = new File(TEST_DIR);
        if (testDir.exists()) {
            FileUtils.deleteDirectory(testDir);
            if (testDir.exists()) {
                FileUtils.forceDelete(testDir);
            }
        }
        assertFalse(testDir.exists());

    }
}
