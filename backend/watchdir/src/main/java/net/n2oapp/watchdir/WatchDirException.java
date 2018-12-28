package net.n2oapp.watchdir;

/**
 * @author iryabov
 * @since 07.12.2015
 */
public class WatchDirException extends RuntimeException {
    public WatchDirException() {
    }

    public WatchDirException(String message) {
        super(message);
    }

    public WatchDirException(String message, Throwable cause) {
        super(message, cause);
    }

    public WatchDirException(Throwable cause) {
        super(cause);
    }
}
