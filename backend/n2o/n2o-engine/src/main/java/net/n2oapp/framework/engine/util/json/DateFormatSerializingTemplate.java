package net.n2oapp.framework.engine.util.json;

/**
 * User: operhod
 * Date: 12.04.14
 * Time: 15:09
 */
public class DateFormatSerializingTemplate<T> {

    public T execute(String dateFormat, DateFormatSerializingCallback<T> callback) {
        try {
            DateFormatHolder.setDateFormat(dateFormat);
            return callback.execute();
        } finally {
            DateFormatHolder.clearDateFormat();
        }

    }

}
