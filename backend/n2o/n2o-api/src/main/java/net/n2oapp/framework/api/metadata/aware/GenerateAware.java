package net.n2oapp.framework.api.metadata.aware;

/**
 * Знание о типах генерируемых кнопок
 */
public interface GenerateAware {

    String[] getGenerate();

    void setGenerate(String[] generate);
}
