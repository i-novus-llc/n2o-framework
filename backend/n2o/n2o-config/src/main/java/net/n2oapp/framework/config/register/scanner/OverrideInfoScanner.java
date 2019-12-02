package net.n2oapp.framework.config.register.scanner;


import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.scan.MetadataScanner;

/**
 * Сканер информации о переопределяющих метаданных
 */
public interface OverrideInfoScanner<I extends SourceInfo> extends MetadataScanner<I> {


}
