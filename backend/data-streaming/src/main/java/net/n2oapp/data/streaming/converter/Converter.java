package net.n2oapp.data.streaming.converter;

/**
 * Created with IntelliJ IDEA.
 * User: operhod
 * Date: 13.09.13
 * Time: 17:51
 * Котвертер коллекции T в байты
 */
public interface Converter<T> {

    /**
     * Начало документа
     */
    byte[] getOpening();

    /**
     * Конец документа
     */
    byte[] getClosing();

    /**
     * Разделитель T
     */
    byte[] getSeparator();

    /**
     * Логика конвертации одног T в байты
     */
    byte[] convert(T t);

}
