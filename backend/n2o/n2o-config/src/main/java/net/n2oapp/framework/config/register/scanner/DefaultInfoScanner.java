package net.n2oapp.framework.config.register.scanner;

import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.scan.MetadataScanner;

/**
 * Сканер информации по умолчанию
 */
public interface DefaultInfoScanner<I extends SourceInfo> extends MetadataScanner<I> {

}
