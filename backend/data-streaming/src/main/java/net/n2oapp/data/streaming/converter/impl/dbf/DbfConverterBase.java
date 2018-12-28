package net.n2oapp.data.streaming.converter.impl.dbf;

import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DbfHeader;
import com.linuxense.javadbf.DbfRecord;
import net.n2oapp.data.streaming.DataStreamingUtil;
import net.n2oapp.data.streaming.converter.EncodingAware;
import net.n2oapp.data.streaming.converter.Converter;

import java.util.List;

import static com.linuxense.javadbf.DbfUtil.END_OF_DATA;

/**
 * Created with IntelliJ IDEA.
 * User: operhod
 * Date: 13.09.13
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class DbfConverterBase<T> implements Converter<T>, EncodingAware {


    protected int numberOfRecords;
    protected List<DBFField> fields;
    protected String encoding = DataStreamingUtil.ENCODING;

    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public DbfConverterBase(int numberOfRecords, List<DBFField> fields) {
        this.numberOfRecords = numberOfRecords;
        this.fields = fields;
    }

    @Override
    public byte[] convert(T t) {
        return new DbfRecord(retrieveValues(t), fields, encoding)
                .toBytes();
    }

    protected abstract List retrieveValues(T t);


    @Override
    public byte[] getSeparator() {
        return DataStreamingUtil.EMPTY_BYTE_ARRAY;
    }

    @Override
    public byte[] getOpening() {
        return new DbfHeader(numberOfRecords, fields)
                .toBytes();
    }

    @Override
    public byte[] getClosing() {
        return new byte[]{END_OF_DATA};
    }


}
