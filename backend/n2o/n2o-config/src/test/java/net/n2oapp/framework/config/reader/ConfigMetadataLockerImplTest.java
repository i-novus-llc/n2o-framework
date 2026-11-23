package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.properties.StaticProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConfigMetadataLockerImplTest {
    @TempDir
    Path configDir;

    private Path lockFile;
    private ConfigMetadataLockerImpl locker;

    @BeforeEach
    void setUp() {
        lockFile = configDir.resolve("lock.tmp");
        locker = new ConfigMetadataLockerImpl(configDir.toString());
    }

    @Test
    void isLockedDetectsExistingFile() throws IOException {
        assertFalse(locker.isLocked());

        Files.createFile(lockFile);

        assertTrue(locker.isLocked());
    }

    @Test
    void lockCreatesLockFile() {
        locker.lock();

        assertTrue(Files.isRegularFile(lockFile));
        assertTrue(locker.isLocked());
    }

    @Test
    void defaultConstructorUsesConfiguredDirectory() {
        try (MockedStatic<StaticProperties> properties = mockStatic(StaticProperties.class)) {
            properties.when(() -> StaticProperties.getProperty("n2o.config.path"))
                    .thenReturn(configDir.toString());

            ConfigMetadataLockerImpl configuredLocker = new ConfigMetadataLockerImpl();
            configuredLocker.lock();

            assertTrue(Files.isRegularFile(lockFile));
            assertTrue(configuredLocker.isLocked());
        }
    }

    @Test
    void lockRejectsExistingLockWithoutChangingFile() throws IOException {
        Files.writeString(lockFile, "existing lock");

        N2oException exception = assertThrows(N2oException.class, locker::lock);

        assertTrue(exception.getMessage().contains(lockFile.toString().replace('\\', '/')));
        assertNull(exception.getCause());
        assertEquals("existing lock", Files.readString(lockFile));
    }

    @Test
    void lockWrapsFileCreationFailure() {
        ConfigMetadataLockerImpl missingDirectoryLocker =
                new ConfigMetadataLockerImpl(configDir.resolve("missing").toString());

        N2oException exception = assertThrows(N2oException.class, missingDirectoryLocker::lock);

        assertInstanceOf(IOException.class, exception.getCause());
        assertFalse(missingDirectoryLocker.isLocked());
    }

    @Test
    void lockRejectsFileCreatedAfterExistenceCheck() throws IOException {
        ConfigMetadataLockerImpl competingLocker = spy(locker);
        // Другой процесс создал блокировку после проверки, но до createNewFile().
        doAnswer(invocation -> {
            Files.writeString(lockFile, "competing lock");
            return false;
        }).when(competingLocker).isLocked();

        N2oException exception = assertThrows(N2oException.class, competingLocker::lock);

        assertInstanceOf(IOException.class, exception.getCause());
        assertEquals("competing lock", Files.readString(lockFile));
    }

    @Test
    void unlockDeletesExistingLockFile() throws IOException {
        Files.createFile(lockFile);

        locker.unlock();

        assertFalse(Files.exists(lockFile));
        assertFalse(locker.isLocked());
    }

    @Test
    void unlockWithoutLockDoesNothing() {
        assertDoesNotThrow(locker::unlock);

        assertFalse(Files.exists(lockFile));
        assertFalse(locker.isLocked());
    }

    @Test
    void unlockWrapsDeletionFailureAndPreservesLock() throws IOException {
        Files.createDirectory(lockFile);
        Path child = Files.writeString(lockFile.resolve("child"), "keep");

        N2oException exception = assertThrows(N2oException.class, locker::unlock);

        assertInstanceOf(DirectoryNotEmptyException.class, exception.getCause());
        assertTrue(locker.isLocked());
        assertEquals("keep", Files.readString(child));
    }
}
