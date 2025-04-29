package net.n2oapp.criteria.dataset;

/**
 * Стратегия слития массивов внутри DataSet
 */
public enum ArrayMergeStrategyEnum {
    /**
     * Слитие с добавлением данных
     */
    MERGE,

    /**
     * Слитие с полной заменой массива
     */
    REPLACE
}
