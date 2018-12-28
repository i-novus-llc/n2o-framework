package net.n2oapp.framework.mvc.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author iryabov
 * @since 24.03.2016
 */
public class BufferedHttpRequest extends HttpServletRequestWrapper {
    private final Reader buffer;

    public BufferedHttpRequest(HttpServletRequest request, Reader buffer) {
        super(request);
        this.buffer = buffer;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(buffer);
    }

    public Reader getBuffer() {
        return buffer;
    }
}
