package net.n2oapp.watchdir;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

import static org.mockito.Mockito.*;

class WatchDirSkipTest {

    @TempDir
    Path testDir;
    private WatchDir watchDir;
    private final FileChangeListener listener = mock(FileChangeListener.class);

    @BeforeEach
    void setUpClass() {
        watchDir = new WatchDir(testDir, true, listener);
    }

    @AfterEach
    void tearDownClass() {
        watchDir.stop();
    }

    @Test
    void skipOnFileIsNotListenedTakeOnIsListened() throws IOException {
        Path file = testDir.resolve("test.txt");
        Path file2 = testDir.resolve("test2.txt");

        watchDir.start();

        watchDir.skipOn(file.toString());

        FileUtils.touch(file.toFile());
        verify(listener, after(2000).never()).fileCreated(file);

        FileUtils.write(file.toFile(), "test", Charset.defaultCharset());
        verify(listener, after(2000).never()).fileModified(file);

        watchDir.takeOn(file.toString());

        FileUtils.touch(file2.toFile());
        verify(listener, timeout(10000).atLeast(1)).fileCreated(file2);

        FileUtils.write(file.toFile(), "test2", Charset.defaultCharset());
        verify(listener, timeout(5000).atLeast(1)).fileModified(file);
    }

    @Test
    void skipOnDirBeforeStartupIsNotListened() throws IOException {
        Path baseExcludeDir = testDir.resolve("exclude1");
        Path excludeDir = baseExcludeDir.resolve("exclude2").resolve("exclude3");
        Path excludeFile = excludeDir.resolve("exclude.txt");
        FileUtils.forceMkdir(excludeDir.toFile());

        watchDir.skipOn(baseExcludeDir.toString());

        watchDir.start();

        FileUtils.touch(excludeFile.toFile());
        verify(listener, after(2000).never()).fileCreated(excludeFile);
        verify(listener, never()).fileModified(excludeFile);
        verify(listener, never()).fileModified(excludeDir);
    }
}
