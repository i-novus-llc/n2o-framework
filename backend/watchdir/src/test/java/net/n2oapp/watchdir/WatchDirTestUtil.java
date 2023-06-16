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
    public final static String TEST_DIR = getTestFolder();
    public final static String TEST_FILE = TEST_DIR + "test.txt";
    public final static String TEST2_FILE = TEST_DIR + "test2.txt";
    public final static String SUB_DIR = TEST_DIR + "dir" + File.separator;
    public final static String SUB2_DIR = SUB_DIR + "sub" + File.separator;
    public final static String SUB2_FILE1 = SUB2_DIR + "file1.txt";
    public final static String SUB2_FILE2 = SUB2_DIR + "file2.txt";

    private static String getTestFolder()
    {
        StringBuilder customTestPath = new StringBuilder();
        customTestPath.append(System.getProperty("user.home"));
        customTestPath.append(File.separator);
        customTestPath.append(WatchDirTest.class.getSimpleName());
        customTestPath.append(File.separator);
        return customTestPath.toString();
    }

    public static void createTestDir() throws IOException {
        clearTestDir();
        assertTrue(new File(TEST_DIR).mkdirs());
    }

    public static void clearTestDir() throws IOException {
        File testDir = new File(TEST_DIR);
        if (testDir.exists())
        {
            FileUtils.deleteDirectory(testDir);
            if (testDir.exists()) {
                FileUtils.forceDelete(testDir);
            }
        }
        assertFalse(testDir.exists());

    }
}
