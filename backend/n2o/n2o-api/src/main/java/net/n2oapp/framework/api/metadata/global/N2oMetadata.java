package net.n2oapp.framework.api.metadata.global;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.aware.RefIdAware;
import org.jdom2.Namespace;

import java.util.List;

/**
 * Базовый класс исходных метаданных считанных из файла
 */
@Getter
@Setter
public abstract class N2oMetadata implements SourceMetadata, IdAware, RefIdAware, NameAware {
    private String namespaceUri;
    private String id;
    private String refId;
    List<Namespace> additionalNamespaces;

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
    }
}
