package net.n2oapp.watchdir;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.Matchers.eq;

import static net.n2oapp.watchdir.WatchDirTestUtil.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * @author iryabov
 * @since 08.12.2015
 */
public class WatchDirSkipTest {

    private WatchDir watchDir;
    private FileChangeListener listener = mock(FileChangeListener.class);


    @BeforeEach
    public void setUpClass() throws Exception
    {
        createTestDir();
        reset(listener);
        watchDir = new WatchDir(Paths.get(TEST_DIR), true, listener);
    }

    @AfterEach
    public void tearDownClass() throws Exception
    {
        watchDir.stop();
        clearTestDir();
    }

    /**
     * Проверяем, что если пропустить файл, то он пропустится один раз, а на следующий сработает
     */
    @Test
    @Disabled
    public void testSkip() throws Exception {
        watchDir.start();
        watchDir.skipOn(TEST_FILE);
        FileUtils.touch(new File(TEST_FILE));
        verify(listener, timeout(200).never()).fileCreated(eq(Paths.get(TEST_FILE)));
        FileUtils.write(new File(TEST_FILE), "test");
        verify(listener, timeout(200).never()).fileModified(eq(Paths.get(TEST_FILE)));
        watchDir.takeOn(TEST_FILE);
        FileUtils.touch(new File(TEST2_FILE));
        verify(listener, timeout(200).atLeast(1)).fileCreated(eq(Paths.get(TEST2_FILE)));
        FileUtils.write(new File(TEST_FILE), "test2");
        verify(listener, timeout(200).atLeast(1)).fileModified(eq(Paths.get(TEST_FILE)));
        watchDir.stop();
    }

    /**
     * Проверяем, что директория не регистрируется, если она в skipOn
     */
    @Test
    @Disabled
    public void testSkipBeforeStart() throws Exception {
        WatchDir watchDir = new WatchDir(Paths.get(TEST_DIR), true, listener);
        String baseExcludeDir = TEST_DIR + "exclude1" + File.separator;
        String excludeDir = baseExcludeDir + "exclude2" + File.separator + "exclude3";
        Path excludeFile = Paths.get(excludeDir + "exclude.txt");
        FileUtils.forceMkdir(new File(excludeDir));
        try {
            watchDir.skipOn(baseExcludeDir);
            watchDir.start();
            FileUtils.touch(excludeFile.toFile());
            verify(listener, timeout(200).never()).fileCreated(eq(excludeFile));
            verify(listener, never()).fileModified(eq(excludeFile));
            verify(listener, never()).fileModified(eq(Paths.get(excludeDir)));
        } finally {
            watchDir.stop();
        }
    }
}
