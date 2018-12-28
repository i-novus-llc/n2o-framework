package net.n2oapp.framework.api.access;

/**
 * Интерфейс обозначающий что данная метаданная подвержена вырезанию секьюрити
 */
public interface Protectable {

    /**
     * Реализация данного метода должна сделать ячейку read-only
     */
    void doReadOnly();

}
