package net.n2oapp.data.streaming.integration.cxf;

import net.n2oapp.data.streaming.stream.DataInputStream;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;


/**
 * Created with IntelliJ IDEA.
 * User: operhod
 * Date: 04.09.13
 * Time: 15:03
 * To change this template use File | Settings | File Templates.
 */
public class InputStreamMessageBodyWriter implements MessageBodyWriter<DataInputStream> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public InputStreamMessageBodyWriter() {

    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public long getSize(DataInputStream resultSetDataInputStream, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(DataInputStream in, Class<?> type, Type genericType,
                        Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders,
                        OutputStream entityStream) throws IOException, WebApplicationException {
        String subtype = mediaType.getSubtype();
        logger.debug("Trying write DataInputStream into servlet OutputStream. Output subtype:{}", subtype);
        IOUtils.copy(in, entityStream);
        in.close();
        entityStream.close();
    }

}
