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

import static net.n2oapp.watchdir.WatchDirTestUtil.*;
import static org.mockito.Mockito.*;

public class WatchDirSkipTest {

    private WatchDir watchDir;
    private final FileChangeListener listener = mock(FileChangeListener.class);

    @BeforeEach
    void setUpClass() throws Exception {
        createTestDir();
        reset(listener);
        watchDir = new WatchDir(Paths.get(TEST_DIR), true, listener);
    }

    @AfterEach
    void tearDownClass() throws Exception {
        watchDir.stop();
        clearTestDir();
    }

    @Test
    void skipOnFileIsNotListenedTakeOnIsListened() throws IOException {
        watchDir.start();

        watchDir.skipOn(TEST_FILE);

        FileUtils.touch(new File(TEST_FILE));
        verify(listener, after(2000).never()).fileCreated(Paths.get(TEST_FILE));

        FileUtils.write(new File(TEST_FILE), "test", Charset.defaultCharset());
        verify(listener, after(2000).never()).fileModified(Paths.get(TEST_FILE));

        watchDir.takeOn(TEST_FILE);

        FileUtils.touch(new File(TEST2_FILE));
        verify(listener, timeout(10000).atLeast(1)).fileCreated(Paths.get(TEST2_FILE));

        FileUtils.write(new File(TEST_FILE), "test2", Charset.defaultCharset());
        verify(listener, timeout(5000).atLeast(1)).fileModified(Paths.get(TEST_FILE));
    }

    @Test
    void skipOnDirBeforeStartupIsNotListened() throws IOException {
        String baseExcludeDir = TEST_DIR + "exclude1" + File.separator;
        String excludeDir = baseExcludeDir + "exclude2" + File.separator + "exclude3";
        Path excludeFile = Paths.get(excludeDir + "exclude.txt");
        FileUtils.forceMkdir(new File(excludeDir));

        watchDir.skipOn(baseExcludeDir);

        watchDir.start();

        FileUtils.touch(excludeFile.toFile());
        verify(listener, after(2000).never()).fileCreated(excludeFile);
        verify(listener, never()).fileModified(excludeFile);
        verify(listener, never()).fileModified(Paths.get(excludeDir));
    }
}
