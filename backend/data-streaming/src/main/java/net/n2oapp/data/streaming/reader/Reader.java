package net.n2oapp.data.streaming.reader;

import java.io.Closeable;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: operhod
 * Date: 13.09.13
 * Time: 17:46
 * Некое перечисление элементов T. Предусмотрено что перечисление может быть курсором, который нужно будет закрыть в конце
 */
public interface Reader<T> extends Iterator<T>, Closeable {

    @Override
    void close();


}
