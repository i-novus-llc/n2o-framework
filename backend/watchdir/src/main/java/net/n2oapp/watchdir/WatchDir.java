package net.n2oapp.watchdir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Watch changes in directory
 * @link http://docs.oracle.com/javase/tutorial/essential/io/examples/WatchDir.java
 */
public class WatchDir {
    private static final Logger logger = LoggerFactory.getLogger(WatchDir.class);
    //minimum time between changes (ms)
    public static final int MOD_TIMEOUT = 100;

    private final boolean recursive;
    private FileChangeListener listener;
    //monitoring directories
    private Set<Path> dirs;

    private final Map<WatchKey,Path> keys = new HashMap<>();
    private final Map<Path, Long> lastModifiedMap = new HashMap<>();
    private final Set<Path> skips = new HashSet<>();

    private WatchService watcher;
    private ExecutorService listenerExec;
    private volatile boolean isRun;

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>)event;
    }

    public WatchDir() {
        this.recursive = true;
    }

    public WatchDir(Path dir, boolean recursive, FileChangeListener listener) {
        this.dirs = new HashSet<>();
        this.dirs.add(dir);
        this.recursive = recursive;
        this.listener = listener;
    }

    //go go go!
    public synchronized void start() {
        throwIfStarted();
        if (listener == null)
            throw new WatchDirException("Listener must not be null!");
        if (dirs == null || dirs.isEmpty()) {
            throw new WatchDirException("Paths must not be empty");
        }
        lastModifiedMap.clear();
        try {
            watcher = FileSystems.getDefault().newWatchService();
            if (recursive) {
                for (Path dir : dirs) {
                    logger.debug("Scanning {} ...", dir);
                    registerAll(dir);
                    logger.debug("Done.");
                }
            } else {
                for (Path dir : dirs) {
                    register(dir);
                }
            }
        } catch (IOException e) {
            throw new WatchDirException(e);
        }
        listenerExec = Executors.newSingleThreadExecutor();
        listenerExec.submit(new WatcherTask(this));
        isRun = true;
        logger.info("WatchDir is started.");
    }

    //stop machine!
    public synchronized void stop() {
        if (!isRun)
            return;
        if (listenerExec != null) {
            listenerExec.shutdown();
        }
        try {
            watcher.close();
        } catch (IOException e) {
            logger.warn("error on stop", e);
        }
        isRun = false;
        logger.info("WatchDir is stopped.");
    }

    //to include in skipOn set
    public void skipOn(String path) {
        synchronized (skips) {
            skips.add(Paths.get(path));
        }
    }

    //to exclude from skipOn set
    public void takeOn(String path) {
        try {
            //Here must pause, so that the thread to take a changes
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new WatchDirException(e.getMessage(), e);
        }
        synchronized (skips) {
            skips.remove(Paths.get(path));
        }
    }

    //set listener for dispatch events
    public synchronized void setListener(FileChangeListener listener) {
        throwIfStarted();
        this.listener = listener;
    }

    //add directory for monitoring
    public synchronized void addPath(String path) {
        throwIfStarted();
        if (dirs == null) {
            dirs = new HashSet<>();
        }
        dirs.add(Paths.get(path));
    }


    private void throwIfStarted() {
        if (isRun) {
            throw new WatchDirException("Already started! Please, first stop watch.");
        }
    }

    /**
     * Register the given directory with the WatchService
     */
    private void register(Path dir) throws IOException {
        synchronized (skips) {
            if (skips.contains(dir))
                return;
        }
        WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        if (logger.isDebugEnabled()) {
            Path prev = keys.get(key);
            if (prev == null) {
                logger.debug("register: {}", dir);
            } else {
                if (!dir.equals(prev)) {
                    logger.debug("update: {} -> {}", prev, dir);
                }
            }
        }
        keys.put(key, dir);
    }

    /**
     * Register the given directory, and all its sub-directories, with the
     * WatchService.
     */
    private void registerAll(final Path start) throws IOException {
        // register directory and sub-directories
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException
            {
                synchronized (skips) {
                    if (skips.contains(dir))
                        return FileVisitResult.SKIP_SUBTREE;
                }
                register(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Process all events for keys queued to the watcher
     */
    void processEvents() {
        for (;;) try {

            // wait for key to be signalled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException | ClosedWatchServiceException x) {
                return;
            }

            Set<Path> takeSkipSet = null;
            synchronized (skips) {
                if (!skips.isEmpty()) {
                    takeSkipSet = new HashSet<>(skips);
                }
            }

            Path dir = keys.get(key);
            if (dir == null) {
                logger.error("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event: key.pollEvents()) {
                WatchEvent.Kind kind = event.kind();

                // TBD - provide example of how OVERFLOW event is handled
                if (kind == OVERFLOW) {
                    continue;
                }

                // Context for directory entry event is the file name of entry
                WatchEvent<Path> ev = cast(event);
                Path name = ev.context();
                Path child = dir.resolve(name);

                //skip muted events
                if (takeSkipSet != null && takeSkipSet.contains(child)) {
                    logger.debug("skip {}", child);
                    continue;
                }
                // event handling
                handleEvent(event, child);

                // if directory is created, and watching recursively, then
                // register it and its sub-directories
                if (recursive && (kind == ENTRY_CREATE)) {
                    try {
                        if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
                            registerAll(child);
                        }
                    } catch (IOException x) {
                        logger.error(x.getMessage(), x);
                    }
                }
            }

            // reset key and remove from set if directory no longer accessible
            boolean valid = key.reset();
            if (!valid) {
                keys.remove(key);

                // all directories are inaccessible
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
        catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }



    private void handleEvent(WatchEvent<?> event, Path child) {
        logger.debug("{}: {}", event.kind().name(), child);
        WatchEvent.Kind kind = event.kind();
        if (kind == ENTRY_CREATE) {
            listener.fileCreated(child);
            Long currentModified = child.toFile().lastModified();
            lastModifiedMap.put(child, currentModified);
        } else if (kind == ENTRY_MODIFY) {
            //do not dispatch modify directories
            if (Files.isDirectory(child))
                return;
            if (Files.notExists(child))
                return;
            Long currentModified = child.toFile().lastModified();
            Long prevModified = lastModifiedMap.get(child);
            if (prevModified != null && (currentModified - prevModified < MOD_TIMEOUT))
                return;
            lastModifiedMap.put(child, currentModified);
            listener.fileModified(child);
        } else if (kind == ENTRY_DELETE) {
            listener.fileDeleted(child);
        }
    }

    static void usage() {
        System.err.println("usage: java WatchDir [-r] dir");
        System.exit(-1);
    }

    public static void main(String[] args) {
        // parse arguments
        if (args.length == 0 || args.length > 2)
            usage();
        boolean recursive = false;
        int dirArg = 0;
        if (args[0].equals("-r")) {
            if (args.length < 2)
                usage();
            recursive = true;
            dirArg++;
        }

        // register directory and process its events
        Path dir = Paths.get(args[dirArg]);
        WatchDir watchDir = new WatchDir(dir, recursive, new FileChangeListener() {
            @Override
            public void fileModified(Path file) {
                System.out.format("modified: %s\n", file);
            }

            @Override
            public void fileCreated(Path file) {
                System.out.format("created: %s\n", file);
            }

            @Override
            public void fileDeleted(Path file) {
                System.out.format("deleted: %s\n", file);
            }
        });
        watchDir.start();
        //create the Scanner
        Scanner terminalInput = new Scanner(System.in);
        //read input
        String s = terminalInput.nextLine();
        if (s != null) {
            watchDir.stop();
            System.exit(0);
        }
    }
}

