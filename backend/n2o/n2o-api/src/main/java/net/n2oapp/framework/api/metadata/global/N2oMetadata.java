package net.n2oapp.framework.api.metadata.global;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.aware.RefIdAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.local.Processable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Базовый класс исходных метаданных считанных из файла
 */
public abstract class N2oMetadata implements SourceMetadata, Processable, IdAware, RefIdAware, NameAware,
        NamespaceUriAware {
    private String namespaceUri;
    private String id;
    private String refId;
    @Deprecated private Map<String, Object> properties;
    @Deprecated private Boolean invalid;
    @Deprecated private String invalidMessage;
    @Deprecated private Class<? extends Exception> exceptionClass;
    @Deprecated private Map<String, Serializable> systemProperties;
    @Deprecated private boolean processable = true;

    @Override
    public String getNamespaceUri() {
        return namespaceUri;
    }

    public void setNamespaceUri(String namespaceUri) {
        this.namespaceUri = namespaceUri;
    }

    @Override
    public boolean isProcessable() {
        return processable;
    }

    @Deprecated
    public void setProcessable(boolean processable) {
        this.processable = processable;
    }


    @Deprecated
    public void addSystemProperty(String key, Serializable value) {
        if (systemProperties == null)
            systemProperties = new HashMap<>();
        systemProperties.put(key, value);
    }

    @Deprecated
    public Serializable getSystemProperty(String key) {
        if (systemProperties == null)
            return null;
        return systemProperties.get(key);
    }

    @Deprecated
    public Map<String, Serializable> getSystemProperties() {
        return systemProperties;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setName(String name) {
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getRefId() {
        return refId;
    }

    @Override
    public void setRefId(String refId) {
        this.refId = refId;
    }

    @Deprecated
    public abstract String getPostfix();

    @Deprecated
    public abstract Class<? extends N2oMetadata> getSourceBaseClass();

    @Deprecated
    public boolean isInvalid() {
        return invalid != null && invalid;
    }

    @Deprecated
    public String getInvalidMessage() {
        return invalidMessage;
    }

    @Deprecated
    public Class<? extends Exception> getExceptionClass() {
        return exceptionClass;
    }

    @Deprecated
    public void doInvalid(String message, Class exceptionClass) {
        invalid = true;
        invalidMessage = message;
        this.exceptionClass = exceptionClass;
    }
}
