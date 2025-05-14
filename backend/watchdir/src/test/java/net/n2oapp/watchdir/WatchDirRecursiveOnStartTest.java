package net.n2oapp.watchdir;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static net.n2oapp.watchdir.WatchDirTestUtil.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * На момент начала тестов создана папка: /dir
 * Слушатель повешан на /
 * Цель: проверить, что события внутри папки /dir ловятся
 */
class WatchDirRecursiveOnStartTest {

    private WatchDir watchDir;
    private final FileChangeListener listener = mock(FileChangeListener.class);

    @BeforeEach
    void setUpClass() throws Exception {
        createTestDir();
        assertTrue(new File(SUB_DIR).mkdirs());
        reset(listener);
        watchDir = new WatchDir(Paths.get(TEST_DIR), true, listener);
    }

    @AfterEach
    void tearDownClass() throws Exception {
        watchDir.stop();
        clearTestDir();
    }

    @Test
    void onlyFileCreatedWasCalledAfterCreatingEmptySubDir() {
        watchDir.start();

        assertTrue(new File(SUB2_DIR).mkdir());

        verify(listener, timeout(10000).atLeast(1)).fileCreated(Paths.get(SUB2_DIR));
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
    }

    @Test
    void onlyFileCreatedWasCalledAfterCreatingSubDir() throws IOException {
        watchDir.start();

        assertTrue(new File(SUB2_DIR).mkdir());

        FileUtils.touch(new File(SUB2_FILE1));
        verify(listener, timeout(10000).atLeast(1)).fileCreated(Paths.get(SUB2_DIR));
        verify(listener, never()).fileCreated(Paths.get(SUB2_FILE1));
        verify(listener, never()).fileDeleted(any(Path.class));
    }

    @Test
    void deletingSubDirWithFilesCallFileModifiedAndFileDeleted() throws IOException {
        assertTrue(new File(SUB2_DIR).mkdir());
        FileUtils.touch(new File(SUB2_FILE1));
        FileUtils.touch(new File(SUB2_FILE2));

        watchDir.start();

        //удаление подпапки с файлом
        FileUtils.deleteDirectory(new File(SUB2_DIR));
        assertFalse(new File(SUB2_DIR).exists());

        //при удалении папки, сначала удаляются файлы внутри неё, поэтому регистрируется событие изменения
        verify(listener, after(2000).never()).fileModified(Paths.get(SUB2_DIR));
        verify(listener, atLeast(1)).fileDeleted(Paths.get(SUB2_DIR));
        verify(listener, never()).fileModified(Paths.get(SUB_DIR));
    }

    @Test
    void fileDeletedWasCalledAfterDeletingEmptySubDir() throws IOException {
        assertTrue(new File(SUB2_DIR).mkdir());

        watchDir.start();

        FileUtils.forceDelete(new File(SUB2_DIR));

        verify(listener, after(2000).never()).fileModified(Paths.get(SUB_DIR));
        verify(listener, timeout(10000).atLeast(1)).fileDeleted(Paths.get(SUB2_DIR));
    }

    @Test
    void fileCreatedAndFileDeletedWasCalledAfterCreatingAndDeletingEmptySubDir() {
        watchDir.start();

        assertTrue(new File(SUB2_DIR).mkdir());

        verify(listener, timeout(10000).atLeast(1)).fileCreated(Paths.get(SUB2_DIR));
        assertTrue(new File(SUB2_DIR).delete());

        verify(listener, timeout(10000).atLeast(1)).fileDeleted(Paths.get(SUB2_DIR));
        verify(listener, never()).fileModified(Paths.get(SUB_DIR));
    }
}
