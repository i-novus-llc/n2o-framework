package net.n2oapp.watchdir;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WatchDir2Test {

    @TempDir
    Path testDir;
    private Path path;
    private WatchDir watchDir;
    private final FileChangeListener listener = mock(FileChangeListener.class);

    @BeforeEach
    void setUpClass() {
        path = testDir.resolve("test.txt");
        watchDir = new WatchDir(testDir, false, listener);
    }

    @AfterEach
    void tearDownClass() {
        watchDir.stop();
    }

    @Test
    void eventOnlyOnCreate() throws IOException {
        watchDir.start();

        FileUtils.touch(path.toFile());

        verify(listener, timeout(10000).atLeast(1)).fileCreated(path);
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
    }

    @Test
    void eventOnlyOnChange() throws IOException {
        FileUtils.touch(path.toFile());

        watchDir.start();

        FileUtils.write(path.toFile(), "test", Charset.defaultCharset());
        verify(listener, timeout(10000).atLeast(1)).fileModified(path);
        verify(listener, never()).fileCreated(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
    }

    @Test
    void eventOnDelete() throws IOException {
        FileUtils.touch(path.toFile());
        watchDir.start();

        assertTrue(path.toFile().delete());
        verify(listener, timeout(10000).atLeast(1)).fileDeleted(path);
        verify(listener, never()).fileCreated(any(Path.class));
        //side effect. Иногда Watcher сначала шлет modified, а потом deleted
    }

    @Test
    void eventsWhenChangeDir() throws IOException {
        Path dir = testDir.resolve("dir");

        //создание пустой папки
        reset(listener);
        watchDir.start();
        FileUtils.forceMkdir(dir.toFile());
        verify(listener, timeout(10000).atLeast(1)).fileCreated(dir);
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
        watchDir.stop();

        //удаление пустой папки
        reset(listener);
        watchDir.start();
        FileUtils.forceDelete(dir.toFile());
        verify(listener, timeout(10000).atLeast(1)).fileDeleted(dir);
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileCreated(any(Path.class));
        watchDir.stop();

        //создание папки с файлом
        reset(listener);
        watchDir.start();
        FileUtils.forceMkdir(dir.toFile());
        FileUtils.touch(dir.resolve("file.txt").toFile());
        verify(listener, timeout(10000).atLeast(1)).fileCreated(dir);
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
        watchDir.stop();

        //удаление папки с файлом
        reset(listener);
        watchDir.start();
        FileUtils.forceDelete(dir.toFile());
        verify(listener, timeout(10000).times(0)).fileCreated(any(Path.class));
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, timeout(10000).atLeast(1)).fileDeleted(dir);
    }

    @Test
    void createChangeDelete() throws IOException {
        watchDir.start();

        FileUtils.touch(path.toFile());
        verify(listener, after(2000).atLeast(1)).fileCreated(path);

        FileUtils.write(path.toFile(), "test", Charset.defaultCharset());
        verify(listener, after(2000).atLeast(1)).fileModified(path);

        FileUtils.forceDelete(path.toFile());
        verify(listener, timeout(10000).atLeast(1)).fileDeleted(path);
    }
}
