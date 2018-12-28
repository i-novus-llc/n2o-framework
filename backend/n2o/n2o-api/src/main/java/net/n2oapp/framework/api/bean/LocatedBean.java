package net.n2oapp.framework.api.bean;

/**
 * Бин, который может содержать информацию о своем положении среди других бинов
 */
public interface LocatedBean {
    /**
     * @return список бинов до которых должен идти текущий бин
     */
    LocatedBean[] getNextBeans();

    /**
     * @return получить список бинов послей которых должен идти текущий бин
     */
    LocatedBean[] getPrevBeans();

    /**
     * @return true - если бин должен находится до всех остальных (исключая getPrevBeans()-бины )
     */
    boolean isBeforeAll();


    /**
     * @return true - если бин должен находится после всех остальных (исключая getNextBeans()-бины )
     */
    boolean isAfterAll();
}
