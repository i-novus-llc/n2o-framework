package net.n2oapp.watchdir;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static net.n2oapp.watchdir.WatchDirTestUtil.*;

/**
 * На момент начала тестов создана папка: /dir
 * Слушатель повешан на /
 * Цель: проверить, что события внутри папки /dir ловятся
 * @author iryabov
 * @since 04.12.2015
 */
public class WatchDirRecursiveOnStartTest {

    private WatchDir watchDir;
    private FileChangeListener listener = mock(FileChangeListener.class);


    @BeforeEach
    public void setUpClass() throws Exception
    {
        createTestDir();
        assertTrue(new File(SUB_DIR).mkdirs());
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
     * Создаём пустую папку
     *  /dir/sub
     * Ожидаем события:
     *  create : /dir/sub
     *  modify : /dir
     */
    @Test
    @Disabled
    public void testCreateEmptySubDir() {
        //создание пустой подпапки
        watchDir.start();
        assertTrue(new File(SUB2_DIR).mkdir());
        
        verify(listener, timeout(200).atLeast(1)).fileCreated(eq(Paths.get(SUB2_DIR)));
        verify(listener, never()).fileModified(any(Path.class));
        verify(listener, never()).fileDeleted(any(Path.class));
        watchDir.stop();
    }

    /**
     * Создаём папку с файлом
     *  /dir/sub
     *  /dir/sub/test.txt
     * Ожидаем события:
     *  create: /dir/sub
     **/
    @Test
    @Disabled
    public void testCreateSubDir() throws IOException {
        //создание подпапки с файлом
        watchDir.start();
        assertTrue(new File(SUB2_DIR).mkdir());
        FileUtils.touch(new File(SUB2_FILE1));
        
        verify(listener, timeout(200).atLeast(1)).fileCreated(eq(Paths.get(SUB2_DIR)));
        //папка не была известна до этого, поэтому изменения файлов в ней не ловятся
        verify(listener, never()).fileCreated(eq(Paths.get(SUB2_FILE1)));
        verify(listener, never()).fileDeleted(any(Path.class));
        watchDir.stop();
    }

    /**
     * В папке /dir/sub создаём:
     *  /dir/sub/test.txt
     * Пишем в файл
     *  /dir/sub/test.txt
     * Ожидаем события:
     *  create : /dir/sub/test.txt
     *  modify : /dir/sub
     */
    @Test
    @Disabled
    public void testChangeFilesInSubDir() throws IOException {
        //изменение файла в подпапке
        //папка создаётся до старта вочдира, поэтому изменения в ней должны ловиться
        assertTrue(new File(SUB2_DIR).mkdir());
        watchDir.start();
        FileUtils.touch(new File(SUB2_FILE1));
        FileUtils.write(new File(SUB2_FILE1), "test");

        verify(listener, timeout(200).atLeast(1)).fileCreated(eq(Paths.get(SUB2_FILE1)));
        verify(listener, never()).fileModified(eq(Paths.get(SUB2_DIR)));
        verify(listener, never()).fileModified(eq(Paths.get(SUB2_FILE1)));
        verify(listener, never()).fileDeleted(any(Path.class));
        watchDir.stop();
    }

    /**
     * Создана папка /dir/sub с двумя файлами
     *  /dir/sub/test1.txt
     *  /dir/sub/test2.txt
     * Удаляем папку
     *  /dir/sub
     * Ожидаем события:
     *  delete : /dir/sub
     **/
    @Test
    @Disabled //не стабильно удаляется папка
    public void testDeleteSubDir() throws IOException {
        //удаление подпапки с файлом
        assertTrue(new File(SUB2_DIR).mkdir());
        FileUtils.touch(new File(SUB2_FILE1));
        FileUtils.touch(new File(SUB2_FILE2));
        watchDir.start();
//        assertTrue(new File(SUB2_DIR).delete());
        assertTrue(new File(SUB2_DIR).delete());
        
        //при удалении папки, сначала удаляются файлы внутри неё, поэтому регистрируется событие изменения
        verify(listener, timeout(200).never()).fileModified(eq(Paths.get(SUB2_DIR)));
        verify(listener, atLeast(1)).fileDeleted(eq(Paths.get(SUB2_DIR)));
        verify(listener, never()).fileModified(eq(Paths.get(SUB_DIR)));
        watchDir.stop();
    }

    /**
     * Создана пустая папка /dir/sub
     * Удаляем папку
     *  /dir/sub
     * Ожидаем события:
     *  delete : /dir/sub
     *  modify : /dir
     **/
    @Test
    @Disabled
    public void testDeleteEmptySubDir() throws IOException {
        //удаление пустой подпапки
        assertTrue(new File(SUB2_DIR).mkdir());
        watchDir.start();
        FileUtils.forceDelete(new File(SUB2_DIR));
        
        verify(listener, timeout(200).never()).fileModified(eq(Paths.get(SUB_DIR)));
        verify(listener, timeout(200).atLeast(1)).fileDeleted(eq(Paths.get(SUB2_DIR)));
        watchDir.stop();
    }

    @Test
    @Disabled
    public void testCreateAndDeleteEmptySubDir() {
        //удаление пустой подпапки
        watchDir.start();
        assertTrue(new File(SUB2_DIR).mkdir());
        assertTrue(new File(SUB2_DIR).delete());

        verify(listener, timeout(200).atLeast(1)).fileCreated(eq(Paths.get(SUB2_DIR)));
        verify(listener, timeout(200).atLeast(1)).fileDeleted(eq(Paths.get(SUB2_DIR)));
        verify(listener, never()).fileModified(eq(Paths.get(SUB_DIR)));
        watchDir.stop();
    }

}
