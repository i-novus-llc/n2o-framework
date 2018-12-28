package net.n2oapp.framework.export.streaming;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.data.streaming.reader.Reader;
import net.n2oapp.properties.StaticProperties;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * Специальный reader большого объема данных. На вход ему подается QueryRequestInfo.
 * Которую он разбивает на подзапрсы. При итерации по данным, он по необходимости подзапрашивает данные,
 * и заполняет буфер.
 */
public class LargeCollectionPageReader implements Reader<DataSet> {

    private QueryRequestInfo baseRequestInfo;
    private LinkedList<DataSet> buffer;
    private int bufferSize;
    private int page;
    private Function<QueryRequestInfo, LinkedList<DataSet>> executor;
    private long maxSize;

    /**
     * @param requestInfo - запрос
     * @param executor    - обработчкик запросов за данными
     * @param bufferSize  - размер буфера данных
     */
    public LargeCollectionPageReader(QueryRequestInfo requestInfo, Function<QueryRequestInfo, LinkedList<DataSet>> executor, int bufferSize) {
        this.bufferSize = bufferSize;
        this.baseRequestInfo = requestInfo;
        this.executor = executor;
        this.page = 1;
        this.maxSize = StaticProperties.getInt("n2o.ui.export.maxCount");
    }

    /**
     * Проверяем текущий буфер. Если он не пустой, берем оттуда след. значение.
     * Если буфер пустой, пытаемся его заполнить и повторяем проверку.
     */
    @Override
    public boolean hasNext() {
        boolean hasNext = bufferIsNotEmpty();
        if (!hasNext) {
            tryFillBuffer();
            hasNext = bufferIsNotEmpty();
        }
        return hasNext;
    }

    private boolean bufferIsNotEmpty() {
        return buffer != null && !buffer.isEmpty();
    }

    /**
     * Тот кто вызывает этот метод уже в курсе что след. элемент доступен.
     * Мы берем из буфера след. элемент и возвращаем его
     */
    @Override
    public DataSet next() {
        if (hasNext()) {
            return getAndRemoveFirst(buffer);
        } else {
            throw new NoSuchElementException();
        }
    }


    @Override
    public void close() {
    }


    /**
     * Вовзращаем первй элемент списка.
     * Из списка этот элемент будет удален
     */
    private static <T> T getAndRemoveFirst(LinkedList<T> list) {
        if (list == null || list.isEmpty())
            return null;
        T first = list.getFirst();
        list.removeFirst();
        return first;
    }


    /**
     * Пытаемся заполнить буфер. Достаем записи для следующей страницы
     */
    private void tryFillBuffer() {
        if ((baseRequestInfo.getCriteria().getSize() != -1 && (page-1)*bufferSize >= baseRequestInfo.getCriteria().getSize()) ||
                (page-1)*bufferSize >= maxSize){
            return;
        }

        QueryRequestInfo newRequest = baseRequestInfo.copy();
        N2oPreparedCriteria newCriteria = new N2oPreparedCriteria(baseRequestInfo.getCriteria());
        newCriteria.setPage(page);
        newCriteria.setSize(bufferSize);
        newRequest.setCriteria(newCriteria);
        this.page++;
        this.buffer = executor.apply(newRequest);
    }


}
