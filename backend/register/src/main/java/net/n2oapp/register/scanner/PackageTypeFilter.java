package net.n2oapp.register.scanner;

import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * User: IRyabov
 * Date: 30.01.12
 * Time: 15:44
 */
public class PackageTypeFilter implements TypeFilter {
    protected String[] packageNames;

    public PackageTypeFilter(String... packageNames) {
        this.packageNames = packageNames;
    }

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        for (String packageName : packageNames) {
            if (metadataReader.getClassMetadata().getClassName().startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }
}
