package net.n2oapp.engine.factory.integration.spring;

/**
 * Бин, который определяет свое положение в списке бинов
 */
public interface OrderedBean {
    /**
     * @return Список бинов до которых должен идти текущий бин
     */
    String[] getNextBeans();

    /**
     * @return Список бинов послей которых должен идти текущий бин
     */
    String[] getPrevBeans();

    /**
     * @return true - если бин должен находится до всех остальных (исключая getPrevBeans()-бины )
     */
    boolean isBeforeAll();


    /**
     * @return true - если бин должен находится после всех остальных (исключая getNextBeans()-бины )
     */
    boolean isAfterAll();
}
