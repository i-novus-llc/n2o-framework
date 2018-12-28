package net.n2oapp.framework.api.metadata.io;

/**
 * Знание о процессоре считывателей элементов
 */
public interface IOProcessorAware {
    void setIOProcessor(IOProcessor processor);
}
