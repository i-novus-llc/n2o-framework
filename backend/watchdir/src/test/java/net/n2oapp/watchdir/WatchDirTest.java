package net.n2oapp.watchdir;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WatchDirTest {
    private static final String TEST_DIR = getTestFolder();
    private Path path = Paths.get(TEST_DIR + "test.txt");
    private WatchDir watchDir;
    private final FileChangeListener listener = mock(FileChangeListener.class);

    private static String getTestFolder() {
        return System.getProperty("user.home") +
                File.separator +
                WatchDirTest.class.getSimpleName() +
                File.separator;
    }

    @BeforeEach
    void setUpClass() throws IOException {
        File testDir = new File(TEST_DIR);
        if (testDir.exists()) {
            FileUtils.forceDelete(testDir);
        }
        assertTrue(testDir.mkdirs());

        reset(listener);

        watchDir = new WatchDir(Paths.get(TEST_DIR), true, listener);
    }

    @AfterEach
    void tearDownClass() throws IOException {
        watchDir.stop();
        File testDir = new File(TEST_DIR);
        if (testDir.exists()) {
            FileUtils.forceDelete(testDir);
        }
        assertFalse(testDir.exists());
    }

    @Test
    void fileCreatedWasCalledAfterCreatingFile() throws IOException {
        watchDir.start();

        FileUtils.touch(new File(path.toString()));

        verify(listener, timeout(10000).atLeast(1)).fileCreated(path);
        verify(listener, never()).fileModified(path);
    }

    @Test
    @Disabled("https://jira.i-novus.ru/browse/NNO-10598")
    void checkingRestartingMonitoring() throws Exception {
        FileUtils.touch(new File(path.toString()));
        verify(listener, after(2000).never()).fileCreated(path);

        reset(listener);
        watchDir.start();

        FileUtils.forceDelete(new File(path.toString()));
        verify(listener, timeout(5000).atLeast(1)).fileDeleted(path);

        reset(listener);
        watchDir.stop();

        FileUtils.touch(new File(path.toString()));
        verify(listener, after(2000).never()).fileCreated(path);

        FileUtils.forceDelete(new File(path.toString()));
        reset(listener);
        watchDir.start();

        FileUtils.touch(new File(path.toString()));
        verify(listener, timeout(10000).atLeast(1)).fileCreated(path);
    }

    @Test
    void fileCreatedWasCalledAfterMovingDirectory() throws IOException {
        watchDir.start();

        path = Paths.get(TEST_DIR + "testFolder");

        FileUtils.forceMkdir(new File(path.toString()));
        verify(listener, timeout(5000).atLeast(1)).fileCreated(path);

        watchDir.stop();
    }

    @Test
    void fileModifiedWasCalledAfterChangingFile() throws IOException {
        FileUtils.touch(new File(path.toString()));

        watchDir.start();

        FileUtils.write(new File(path.toString()), "test", Charset.defaultCharset());
        verify(listener, timeout(5000).atLeast(1)).fileModified(path);

        FileUtils.write(new File(path.toString()), "test2", Charset.defaultCharset());
        verify(listener, timeout(5000).atLeast(1)).fileModified(path);

        watchDir.stop();
    }

    @Test
    void fileDeletedWasCalledAfterDeletingFile() throws IOException {
        FileUtils.touch(new File(path.toString()));
        watchDir.start();

        assertTrue(new File(path.toString()).delete());
        verify(listener, timeout(5000).atLeast(1)).fileDeleted(path);

        watchDir.stop();
    }

    @Test
    void monitoringIsNotWorkingBeforeStart() throws IOException {
        FileUtils.touch(new File(path.toString()));
        verify(listener, after(100).never()).fileCreated(any(Path.class));

        watchDir.start();

        FileUtils.forceDelete(new File(path.toString()));
        verify(listener, timeout(5000).atLeast(1)).fileDeleted(path);

        watchDir.stop();
    }

    @Test
    void incorrectCreate() {
        watchDir = new WatchDir();
        //старт без listener
        try {
            watchDir.start();
            assert false;
        } catch (WatchDirException e) {
            assert true;
        }
        watchDir.setListener(listener);
        //старт без единого пути
        try {
            watchDir.start();
            assert false;
        } catch (WatchDirException e) {
            assert true;
        }
        watchDir.addPath(TEST_DIR);
        //успешный старт
        try {
            watchDir.start();
            assert true;
        } catch (WatchDirException e) {
            assert false;
        }
        //повторный старт
        try {
            watchDir.start();
            assert false;
        } catch (WatchDirException e) {
            assert true;
        }
        watchDir.stop();
    }

    @Test
    void immutableAfterStart() {
        watchDir = new WatchDir();
        watchDir.setListener(listener);
        watchDir.addPath(TEST_DIR);
        watchDir.start();
        //смена listener после старта
        try {
            watchDir.setListener(listener);
            assert false;
        } catch (WatchDirException e) {
            assert true;
        }
        //добавление пути после старта
        try {
            watchDir.addPath(TEST_DIR);
            assert false;
        } catch (WatchDirException e) {
            assert true;
        }
    }
}
