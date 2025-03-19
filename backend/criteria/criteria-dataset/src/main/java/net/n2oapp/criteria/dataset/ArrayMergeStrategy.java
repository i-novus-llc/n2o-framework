package net.n2oapp.criteria.dataset;

/**
 * Стратегия слития массивов внутри DataSet
 */
public enum ArrayMergeStrategy {
    /**
     * Слитие с добавлением данных
     */
    MERGE,

    /**
     * Слитие с полной заменой массива
     */
    REPLACE
}
