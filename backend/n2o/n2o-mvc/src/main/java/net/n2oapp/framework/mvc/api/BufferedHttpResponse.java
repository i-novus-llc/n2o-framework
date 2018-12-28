package net.n2oapp.framework.mvc.api;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * @author iryabov
 * @since 15.12.2015
 */
public class BufferedHttpResponse extends HttpServletResponseWrapper {

    private final Writer buffer;

    public BufferedHttpResponse(HttpServletResponse response, Writer buffer) {
        super(response);
        this.buffer = buffer;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(buffer);
    }

    public Writer getBuffer() {
        return buffer;
    }
}