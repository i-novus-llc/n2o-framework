package net.n2oapp.framework.mvc.api;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author iryabov
 * @since 17.12.2015
 */
public class CachingPrintWriter extends Writer {
    private final Writer origin;
    private final CharArrayWriter cache = new CharArrayWriter();

    public CachingPrintWriter(Writer origin) {
        this.origin = origin;
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        origin.write(cbuf, off, len);
        cache.write(cbuf, off, len);
    }

    @Override
    public void flush() throws IOException {
        origin.flush();
    }

    @Override
    public void close() throws IOException {
        origin.close();
    }

    public String getCache() {
        return cache.toString();
    }
}
