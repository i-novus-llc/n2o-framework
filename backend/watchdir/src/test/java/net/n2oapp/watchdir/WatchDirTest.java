package net.n2oapp.watchdir;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

/**
 * User: Belyaev Gleb
 * Date: 14.11.13
 */
public class WatchDirTest
{
    private final static String TEST_DIR = getTestFolder();
    private Path path = Paths.get(TEST_DIR + "test.txt");
    private WatchDir watchDir;
    private FileChangeListener listener = mock(FileChangeListener.class);

    private static String getTestFolder()
    {
        StringBuilder customTestPath = new StringBuilder();
        customTestPath.append(System.getProperty("user.home"));
        customTestPath.append(File.separator);
        customTestPath.append(WatchDirTest.class.getSimpleName());
        customTestPath.append(File.separator);
        return customTestPath.toString();
    }

//    @Before
    public void setUpClass() throws Exception
    {
        File testDir = new File(TEST_DIR);
        if (testDir.exists())
        {
            FileUtils.forceDelete(testDir);
        }
        assertTrue(testDir.mkdirs());

        reset(listener);

        watchDir = new WatchDir(Paths.get(TEST_DIR), true, listener);
    }

//    @After
    public void tearDownClass() throws Exception
    {
        watchDir.stop();
        File testDir = new File(TEST_DIR);
        if (testDir.exists())
        {
            FileUtils.forceDelete(testDir);
        }
        assertFalse(testDir.exists());
    }

    /**
     * проверка срабатывания в подпапках
     */
    @Test
    @Disabled
    void testWithRecursive() throws Exception
    {
        watchDir.start();

        FileUtils.touch(new File(path.toString()));
        verify(listener, timeout(100).atLeast(1)).fileCreated(eq(path));
        verify(listener, timeout(100).never()).fileModified(eq(path));

        watchDir.stop();
    }

    /**
     * добавление новой папки
     */
//    @Test
//    public void testAddNewPath() throws Exception
//    {
//        watchDir.start();
//        FileUtils.touch(new File(path.toString()));
//        verify(listener, timeout(100).atLeast(1)).fileCreated(eq(path));
//
//        StringBuilder customTestPath = new StringBuilder();
//        customTestPath.append(System.getProperty("user.home"));
//        customTestPath.append(File.separator);
//        customTestPath.append(WatchDirTest.class.getSimpleName());
//        customTestPath.append("_2");
//        customTestPath.append(File.separator);
//
//        String newPath = customTestPath.toString();
//        File testDir = new File(newPath);
//        if (testDir.exists())
//        {
//            FileUtils.forceDelete(testDir);
//        }
//        assertTrue(testDir.mkdirs());
//
//        watchDir.addPath(newPath);
//        String newFile = newPath + "testFile2.txt";
//        FileUtils.touch(new File(newFile));
//        verify(listener, timeout(100).atLeast(1)).fileCreated(eq(Paths.get(newFile)));
//
//        FileUtils.forceDelete(testDir);
//
//        watchDir.stop();
//    }

    /**
     * проверить что работает перезапуск
     * <p/>
     * не реагирует на события
     * запуск
     * реагирует на события
     * остановка
     * не реагирует на события
     * запуск
     * реагирует на события
     *
     */
    @Test
    @Disabled
    void testRestartMonitoring() throws Exception
    {
        FileUtils.touch(new File(path.toString()));
        verify(listener, timeout(100).never()).fileCreated(eq(path));

        reset(listener);
        watchDir.start();

        FileUtils.forceDelete(new File(path.toString()));
        verify(listener, timeout(100).atLeast(1)).fileDeleted(eq(path));

        reset(listener);
        watchDir.stop();

        FileUtils.touch(new File(path.toString()));
        verify(listener, timeout(100).never()).fileCreated(eq(path));
    }

    /**
     * событие файла
     * проверить что события файла
     *
     */
    @Test
    @Disabled
    void testChangeIsFile() throws Exception
    {
        watchDir.start();

        FileUtils.touch(new File(path.toString()));
        verify(listener, timeout(100).atLeast(1)).fileCreated(eq(path));

        watchDir.stop();
    }

    /**
     * событие каталога
     * проверить что событие каталога
     *
     */
    @Test
    @Disabled
    void testChangeIsDirectory() throws Exception
    {
        watchDir.start();

        path = Paths.get(TEST_DIR + "testFolder");

        FileUtils.forceMkdir(new File(path.toString()));
        verify(listener, timeout(100).atLeast(1)).fileCreated(eq(path));

        watchDir.stop();
    }

    /**
     * проверка событие создания
     * проверить что было событие на создания
     * как side эффект, после события создания следует событие изменения
     *
     */
    @Test
    @Disabled
    void testEventOnCreate() throws Exception
    {
        watchDir.start();

        FileUtils.touch(new File(path.toString()));
        verify(listener, timeout(100).atLeast(1)).fileCreated(eq(path));
        verify(listener, timeout(100).never()).fileModified(eq(path));

        watchDir.stop();
    }

    /**
     * проверка событие изменения
     * проверить что было событие изменения
     *
     */
    @Test
    @Disabled //todo почему то не срабатывает тест на https://ci.i-novus.ru/view/util/job/watchdir.master.build/lastBuild/net.n2oapp.watchdir$watchdir/testReport/net.n2oapp.watchdir/WatchDirTest/testEventOnChange/
    void testEventOnChange() throws Exception
    {
        FileUtils.touch(new File(path.toString()));

        watchDir.start();

        FileUtils.write(new File(path.toString()), "test");
        Thread.sleep(100);
        verify(listener, timeout(100).atLeast(1)).fileModified(eq(path));

        reset(listener);
        FileUtils.write(new File(path.toString()), "test2");
        verify(listener, timeout(100).atLeast(1)).fileModified(eq(path));

        watchDir.stop();
    }

    /**
     * проверка событие удаление
     * проверить что было событие удаления
     *
     */
    @Test
    @Disabled
    void testEventOnDelete() throws Exception
    {
        FileUtils.touch(new File(path.toString()));
        watchDir.start();

        assertTrue(new File(path.toString()).delete());
        verify(listener, timeout(100).atLeast(1)).fileDeleted(eq(path));

        watchDir.stop();
    }

    /**
     * Режим запуска, не срабатывали события а после запуска срабатывают
     *
     */
    @Test
    @Disabled
    void testStartMonitoring() throws Exception
    {
        FileUtils.touch(new File(path.toString()));
        verify(listener, timeout(100).never()).fileCreated(any(Path.class));

        watchDir.start();

        FileUtils.forceDelete(new File(path.toString()));
        verify(listener, timeout(100).atLeast(1)).fileDeleted(eq(path));

        watchDir.stop();
    }

    /**
     * после остановки события не срабатывают
     */
    @Test
    @Disabled
    void testStopMonitoring() throws Exception
    {
        watchDir.start();

        FileUtils.touch(new File(path.toString()));
        verify(listener, timeout(100).atLeast(1)).fileCreated(eq(path));

        reset(listener);
        watchDir.stop();

        FileUtils.forceDelete(new File(path.toString()));
        verify(listener, timeout(100).never()).fileModified(any(Path.class));
    }

    @Test
    @Disabled
    void testEqPath() {
        Path path1 = Paths.get(TEST_DIR + "test.txt");
        Path path2 = Paths.get(TEST_DIR + "test.txt");
        Path path3 = Paths.get(TEST_DIR);

        assertEquals(path1, path2);
        assertTrue(path1.equals(path2));

        assertNotEquals(path1, path3);
        assertFalse(path1.equals(path3));
    }

    @Test
    @Disabled
    void testIncorrectCreate() {
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
    @Disabled
    void testImmutableAfterStart() {
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
