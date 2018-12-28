package net.n2oapp.framework.api.register.scan;

import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.register.SourceInfo;

import java.util.List;

/**
 * Фабрика сканеров метаданных
 */
public interface MetadataScannerFactory extends MetadataFactory<MetadataScanner> {

    /**
     * Сканировать метаданные
     * @return Список информации о метаданных для регистрации
     */
    List<? extends SourceInfo> scan();

}
