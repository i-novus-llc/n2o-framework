package net.n2oapp.watchdir;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * На момент начала тестов создана папка: /dir
 * Слушатель повешан на /
 * Цель: проверить, что события внутри папки /dir ловятся
 */
class WatchDirRecursiveOnStartTest {

    @TempDir
    Path testDir;
    private Path subDir;
    private Path sub2Dir;
    private WatchDir watchDir;
    private final FileChangeListener listener = mock(FileChangeListener.class);

    @BeforeEach
    void setUpClass() throws IOException {
        subDir = testDir.resolve("dir");
        sub2Dir = subDir.resolve("sub");
        FileUtils.forceMkdir(subDir.toFile());
        watchDir = new WatchDir(testDir, true, listener);
    }

    @AfterEach
    void tearDownClass() {
        watchDir.stop();
    }

    @Test
    void onlyFileCreatedWasCalledAfterCreatingEmptySubDir() {
        watchDir.start();

        assertTrue(sub2Dir.toFile().mkdir());

        verify(listener, timeout(10000).atLeast(1)).fileCreated(sub2Dir);
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
    }

    @Test
    void onlyFileCreatedWasCalledAfterCreatingSubDir() throws IOException {
        watchDir.start();

        assertTrue(sub2Dir.toFile().mkdir());

        Path sub2File1 = sub2Dir.resolve("file1.txt");
        FileUtils.touch(sub2File1.toFile());
        verify(listener, timeout(10000).atLeast(1)).fileCreated(sub2Dir);
        verify(listener, never()).fileCreated(sub2File1);
        verify(listener, never()).fileDeleted(any(Path.class));
    }

    @Test
    void deletingSubDirWithFilesCallFileModifiedAndFileDeleted() throws IOException {
        assertTrue(sub2Dir.toFile().mkdir());
        FileUtils.touch(sub2Dir.resolve("file1.txt").toFile());
        FileUtils.touch(sub2Dir.resolve("file2.txt").toFile());

        watchDir.start();

        //удаление подпапки с файлом
        FileUtils.deleteDirectory(sub2Dir.toFile());
        assertFalse(sub2Dir.toFile().exists());

        //при удалении папки, сначала удаляются файлы внутри неё, поэтому регистрируется событие изменения
        verify(listener, after(2000).never()).fileModified(sub2Dir);
        verify(listener, atLeast(1)).fileDeleted(sub2Dir);
        verify(listener, never()).fileModified(subDir);
    }

    @Test
    void fileDeletedWasCalledAfterDeletingEmptySubDir() throws IOException {
        assertTrue(sub2Dir.toFile().mkdir());

        watchDir.start();

        FileUtils.forceDelete(sub2Dir.toFile());

        verify(listener, after(2000).never()).fileModified(subDir);
        verify(listener, timeout(10000).atLeast(1)).fileDeleted(sub2Dir);
    }

    @Test
    void fileCreatedAndFileDeletedWasCalledAfterCreatingAndDeletingEmptySubDir() {
        watchDir.start();

        assertTrue(sub2Dir.toFile().mkdir());

        verify(listener, timeout(10000).atLeast(1)).fileCreated(sub2Dir);
        assertTrue(sub2Dir.toFile().delete());

        verify(listener, timeout(10000).atLeast(1)).fileDeleted(sub2Dir);
        verify(listener, never()).fileModified(subDir);
    }
}
