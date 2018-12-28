package net.n2oapp.data.streaming.converter.impl.csv;

import net.n2oapp.data.streaming.DataStreamingUtil;
import net.n2oapp.data.streaming.converter.EncodingAware;
import net.n2oapp.data.streaming.converter.Converter;

import java.util.Collection;

import static net.n2oapp.data.streaming.DataStreamingUtil.getBytes;

/**
 * Created with IntelliJ IDEA.
 * User: operhod
 * Date: 13.09.13
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class CsvConverterBase<T> implements Converter<T>, EncodingAware {

    protected String columnSeparator = ";";
    protected String encoding = DataStreamingUtil.ENCODING;

    public void setColumnSeparator(String columnSeparator) {
        this.columnSeparator = columnSeparator;
    }

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public byte[] convert(T t) {
        StringBuilder result = new StringBuilder();
        boolean begin = true;
        for (String value : retrieveValues(t)) {

            if (!begin)
                result.append(columnSeparator);
            begin = false;

            result.append(value != null ? value : "");
        }
        return DataStreamingUtil.getBytes(result.toString(), encoding);
    }

    protected abstract Collection<String> retrieveValues(T t);

    @Override
    public byte[] getSeparator() {
        return new byte[]{'\r', '\n'};
    }

    @Override
    public byte[] getOpening() {
        return DataStreamingUtil.EMPTY_BYTE_ARRAY;
    }

    @Override
    public byte[] getClosing() {
        return DataStreamingUtil.EMPTY_BYTE_ARRAY;
    }


}
