package net.n2oapp.framework.api.metadata.local;

/**
 * Флаг, показывающий является ли метаданная обрабатываемой.
 * Не обрабатываемой является только страница с ошибкой, ее не надо проверять на права доступа
 */
@Deprecated //нужно упразднить, похоже на костыль
public interface Processable {
    @Deprecated
    boolean isProcessable();
}
