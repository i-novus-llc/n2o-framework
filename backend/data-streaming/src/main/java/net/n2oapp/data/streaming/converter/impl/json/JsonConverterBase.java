package net.n2oapp.data.streaming.converter.impl.json;

import net.n2oapp.data.streaming.converter.Converter;

/**
 * Created with IntelliJ IDEA.
 * User: operhod
 * Date: 13.09.13
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class JsonConverterBase<T> implements Converter<T> {


    protected byte[] initDefaultSeparator() {
        return new byte[]{','};
    }

    protected byte[] initDefaultOpening() {
        return new byte[]{'['};
    }

    protected byte[] initDefaultClosing() {
        return new byte[]{']'};
    }

}
