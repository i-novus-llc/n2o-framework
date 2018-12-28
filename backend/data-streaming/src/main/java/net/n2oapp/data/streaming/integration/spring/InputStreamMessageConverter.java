package net.n2oapp.data.streaming.integration.spring;

import net.n2oapp.data.streaming.stream.DataInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created with IntelliJ IDEA.
 * User: operhod
 * Date: 09.09.13
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */
public class InputStreamMessageConverter extends AbstractHttpMessageConverter<DataInputStream> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String JSON_CONTENT_SUBTYPE = "json";
    public static final String XML_CONTENT_SUBTYPE = "xml";
    public static final String CSV_CONTENT_SUBTYPE = "csv";
    public static final String EXCEL_CONTENT_SUBTYPE = "vnd.ms-excel";
    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public InputStreamMessageConverter() {
        super(
                new org.springframework.http.MediaType("application", JSON_CONTENT_SUBTYPE, DEFAULT_CHARSET),
                new org.springframework.http.MediaType("application", EXCEL_CONTENT_SUBTYPE, DEFAULT_CHARSET),
                new org.springframework.http.MediaType("text", CSV_CONTENT_SUBTYPE, DEFAULT_CHARSET),
                new org.springframework.http.MediaType("application", XML_CONTENT_SUBTYPE, DEFAULT_CHARSET));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected DataInputStream readInternal(Class<? extends DataInputStream> clazz, HttpInputMessage inputMessage)
            throws HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(DataInputStream in, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        HttpHeaders httpHeaders = outputMessage.getHeaders();
        String subtype = httpHeaders.getContentType().getSubtype();
        logger.debug("Trying write DataInputStream into servlet OutputStream. Output subtype:{}", subtype);
        IOUtils.copy(in, outputMessage.getBody());
        in.close();
        outputMessage.getBody().close();
    }

}
