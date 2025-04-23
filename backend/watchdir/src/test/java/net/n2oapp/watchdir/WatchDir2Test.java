package net.n2oapp.watchdir;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WatchDir2Test {
    private final static String TEST_DIR = getTestFolder();
    private final Path path = Paths.get(TEST_DIR + "test.txt");
    private WatchDir watchDir;
    private final FileChangeListener listener = mock(FileChangeListener.class);

    private static String getTestFolder() {
        StringBuilder customTestPath = new StringBuilder();

        customTestPath.append(System.getProperty("user.home"));
        customTestPath.append(File.separator);
        customTestPath.append(WatchDirTest.class.getSimpleName());
        customTestPath.append(File.separator);

        return customTestPath.toString();
    }

    @BeforeEach
    void setUpClass() throws IOException {
        File testDir = new File(TEST_DIR);
        if (testDir.exists()) {
            FileUtils.forceDelete(testDir);
        }
        assertTrue(testDir.mkdirs());
        reset(listener);

        watchDir = new WatchDir(Paths.get(TEST_DIR), false, listener);
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
    void eventOnlyOnCreate() throws IOException {
        watchDir.start();

        FileUtils.touch(new File(path.toString()));

        verify(listener, timeout(10000).atLeast(1)).fileCreated(path);
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
    }

    @Test
    void eventOnlyOnChange() throws IOException {
        FileUtils.touch(new File(path.toString()));

        watchDir.start();

        FileUtils.write(new File(path.toString()), "test", Charset.defaultCharset());
        verify(listener, timeout(10000).atLeast(1)).fileModified(path);
        verify(listener, never()).fileCreated(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
    }

    @Test
    void eventOnDelete() throws IOException {
        FileUtils.touch(new File(path.toString()));
        watchDir.start();

        assertTrue(new File(path.toString()).delete());
        verify(listener, timeout(10000).atLeast(1)).fileDeleted(path);
        verify(listener, never()).fileCreated(any(Path.class));
        //side effect. Иногда Watcher сначала шлет modified, а потом deleted
    }

    @Test
    void eventsWhenChangeDir() throws IOException {
        String dir = TEST_DIR + "dir" + File.separator;

        //создание пустой папки
        reset(listener);
        watchDir.start();
        FileUtils.forceMkdir(new File(dir));
        verify(listener, timeout(10000).atLeast(1)).fileCreated(Paths.get(dir));
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
        watchDir.stop();

        //удаление пустой папки
        reset(listener);
        watchDir.start();
        FileUtils.forceDelete(new File(dir));
        verify(listener, timeout(10000).atLeast(1)).fileDeleted(Paths.get(dir));
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileCreated(any(Path.class));
        watchDir.stop();

        //создание папки с файлом
        reset(listener);
        watchDir.start();
        FileUtils.forceMkdir(new File(dir));
        FileUtils.touch(new File(dir + "file.txt"));
        verify(listener, timeout(10000).atLeast(1)).fileCreated(Paths.get(dir));
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
        watchDir.stop();

        //удаление папки с файлом
        reset(listener);
        watchDir.start();
        FileUtils.forceDelete(new File(dir));
        verify(listener, timeout(10000).times(0)).fileCreated(any(Path.class));
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, timeout(10000).atLeast(1)).fileDeleted(Paths.get(dir));
    }

    @Test
    void createChangeDelete() throws IOException {
        watchDir.start();

        FileUtils.touch(new File(path.toString()));
        verify(listener, after(2000).atLeast(1)).fileCreated(path);

        FileUtils.write(new File(path.toString()), "test", Charset.defaultCharset());
        verify(listener, after(2000).atLeast(1)).fileModified(path);

        FileUtils.forceDelete(new File(path.toString()));
        verify(listener, timeout(10000).atLeast(1)).fileDeleted(path);
    }
}
