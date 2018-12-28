package net.n2oapp.data.streaming;

import java.nio.charset.Charset;

/**
 * @author operehod
 * @since 29.10.2015
 */
public class DataStreamingUtil {

    public final static String ENCODING = "UTF-8";

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    public static byte[] getBytes(String s) {
        return getBytes(s, ENCODING);
    }

    public static byte[] getBytes(String s, String encoding) {
        return s.getBytes(Charset.forName(encoding));
    }


}
