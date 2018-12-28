package net.n2oapp.framework.api.register.scan;

import net.n2oapp.framework.api.register.SourceInfo;

import java.util.List;

@FunctionalInterface
public interface MetadataScanner<I extends SourceInfo> {
    List<I> scan();
}
