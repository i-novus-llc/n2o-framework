package net.n2oapp.data.streaming.stream;

import net.n2oapp.data.streaming.converter.Converter;
import net.n2oapp.data.streaming.reader.Reader;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: operhod
 * Date: 13.09.13
 * Time: 17:36
 * По сути это API data-streaming.
 * Поток, который формируется из некоторого перечисления элементов T, и конвертатора, который превращает T в байты.
 */
public class DataInputStream<T> extends InputStream {
    private int position;
    private boolean separator = false;
    private byte[] buffer;
    private boolean end = false;

    private Converter converter;
    private Reader reader;

    public DataInputStream(Converter<T> converter, Reader<T> reader) {
        this.converter = converter;
        this.reader = reader;
    }


    @Override
    public int read() {
        if (end) {
            if (position < buffer.length) {
                return buffer[position++] & (0xff);
            } else return -1;
        } else if (buffer == null) {
            if (!reader.hasNext()) {
                return -1;
            } else {
                buffer = converter.getOpening();
                position = 0;
                if (position >= buffer.length) {
                    separator = true;
                    buffer = converter.convert(reader.next());
                }
                return buffer[position++] & (0xff);
            }
        } else {
            if (position < buffer.length) {
                return buffer[position++] & (0xff);
            } else {
                if (!reader.hasNext()) {
                    end = true;
                    buffer = converter.getClosing();
                    position = 0;
                    if (position >= buffer.length) return -1;
                    return buffer[position++] & (0xff);
                } else {
                    if (!separator) {
                        buffer = converter.convert(reader.next());
                        separator = true;
                    } else {
                        buffer = converter.getSeparator();
                        separator = false;
                    }
                    position = 0;
                    if (position >= buffer.length) buffer = converter.convert(reader.next());
                    return buffer[position++] & (0xff);
                }
            }
        }
    }

    @Override
    public void close() {
        reader.close();
    }


}
