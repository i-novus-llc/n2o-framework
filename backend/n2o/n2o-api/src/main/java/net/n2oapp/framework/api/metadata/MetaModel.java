package net.n2oapp.framework.api.metadata;

import net.n2oapp.framework.api.exception.N2oException;

import java.util.Objects;

/**
 * Мета информация о метаданных
 */
@Deprecated
public class MetaModel {
    private String type;

    private Class<? extends SourceMetadata> sourceMetadataClass;
    private Class<? extends CompiledMetadata> compileMetadataClass;
    private Class<? extends ClientMetadata> clientMetadataClass;

    private Class<? extends SourceMetadata> baseSourceMetadataClass;
    private Class<? extends CompiledMetadata> baseCompileMetadataClass;
    private Class<? extends ClientMetadata> baseClientMetadataClass;

    private boolean error = false;
    private boolean blank = false;

    public MetaModel(Class<? extends CompiledMetadata> compileMetadataClass) {
        this.compileMetadataClass = compileMetadataClass;
        CompiledMetadata compiledMetadata = produceCompiledMetadata();
        this.baseCompileMetadataClass = compiledMetadata.getCompiledBaseClass();
        this.sourceMetadataClass = compiledMetadata.getSourceClass();
        SourceMetadata sourceMetadata = produceSourceMetadata();
        this.baseSourceMetadataClass = sourceMetadata.getSourceBaseClass();
        this.type = sourceMetadata.getMetadataType();
    }

    public MetaModel(String type,
                     Class<? extends SourceMetadata> sourceMetadataClass,
                     Class<? extends CompiledMetadata> compileMetadataClass,
                     Class<? extends ClientMetadata> clientMetadataClass,
                     Class<? extends SourceMetadata> baseSourceMetadataClass,
                     Class<? extends CompiledMetadata> baseCompileMetadataClass,
                     Class<? extends ClientMetadata> baseClientMetadataClass,
                     boolean error, boolean blank) {
        this.type = type;
        this.sourceMetadataClass = sourceMetadataClass;
        this.compileMetadataClass = compileMetadataClass;
        this.clientMetadataClass = clientMetadataClass;
        this.baseSourceMetadataClass = baseSourceMetadataClass;
        this.baseCompileMetadataClass = baseCompileMetadataClass;
        this.baseClientMetadataClass = baseClientMetadataClass;
        this.error = error;
        this.blank = blank;
    }

    public MetaModel(String type,
                     Class<? extends SourceMetadata> sourceMetadataClass,
                     Class<? extends CompiledMetadata> compileMetadataClass,
                     Class<? extends ClientMetadata> clientMetadataClass,
                     boolean error, boolean blank) {
        this.type = type;
        this.sourceMetadataClass = sourceMetadataClass;
        this.compileMetadataClass = compileMetadataClass;
        this.clientMetadataClass = clientMetadataClass;
        this.baseSourceMetadataClass = sourceMetadataClass;
        this.baseCompileMetadataClass = compileMetadataClass;
        this.baseClientMetadataClass = clientMetadataClass;
        this.error = error;
        this.blank = blank;
    }

    public MetaModel(String type,
                     Class<? extends SourceMetadata> sourceMetadataClass,
                     Class<? extends CompiledMetadata> compileMetadataClass,
                     Class<? extends ClientMetadata> clientMetadataClass) {
        this.type = type;
        this.sourceMetadataClass = sourceMetadataClass;
        this.compileMetadataClass = compileMetadataClass;
        this.clientMetadataClass = clientMetadataClass;
        this.baseSourceMetadataClass = sourceMetadataClass;
        this.baseCompileMetadataClass = compileMetadataClass;
        this.baseClientMetadataClass = clientMetadataClass;
    }

    @SuppressWarnings("unchecked")
    public <C extends CompiledMetadata> C produceCompiledMetadata() {
        try {
            return (C) compileMetadataClass.newInstance();
        } catch (Exception e) {
            throw new N2oException(
                    "Compilable class " + compileMetadataClass.getName() + " must have default constructor");
        }
    }

    @SuppressWarnings("unchecked")
    public <C extends SourceMetadata> C produceSourceMetadata() {
        try {
            return (C) sourceMetadataClass.newInstance();
        } catch (Exception e) {
            throw new N2oException(
                    "Compilable class " + sourceMetadataClass.getName() + " must have default constructor");
        }
    }

    public String getType() {
        return type;
    }

    public Class<? extends SourceMetadata> getSourceMetadataClass() {
        return sourceMetadataClass;
    }

    public Class<? extends CompiledMetadata> getCompileMetadataClass() {
        return compileMetadataClass;
    }

    public Class<? extends ClientMetadata> getClientMetadataClass() {
        return clientMetadataClass;
    }

    public Class<? extends SourceMetadata> getBaseSourceMetadataClass() {
        return baseSourceMetadataClass;
    }

    public Class<? extends CompiledMetadata> getBaseCompileMetadataClass() {
        return baseCompileMetadataClass;
    }

    public Class<? extends ClientMetadata> getBaseClientMetadataClass() {
        return baseClientMetadataClass;
    }

    public boolean isError() {
        return error;
    }

    public boolean isBlank() {
        return blank;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetaModel)) return false;
        MetaModel metaModel = (MetaModel) o;
        return error == metaModel.error &&
                blank == metaModel.blank &&
                Objects.equals(type, metaModel.type) &&
                Objects.equals(sourceMetadataClass, metaModel.sourceMetadataClass) &&
                Objects.equals(compileMetadataClass, metaModel.compileMetadataClass) &&
                Objects.equals(clientMetadataClass, metaModel.clientMetadataClass) &&
                Objects.equals(baseSourceMetadataClass, metaModel.baseSourceMetadataClass) &&
                Objects.equals(baseCompileMetadataClass, metaModel.baseCompileMetadataClass) &&
                Objects.equals(baseClientMetadataClass, metaModel.baseClientMetadataClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type,
                sourceMetadataClass,
                compileMetadataClass,
                clientMetadataClass,
                baseSourceMetadataClass,
                baseCompileMetadataClass,
                baseClientMetadataClass,
                error,
                blank);
    }

    @Override
    public String toString() {
        return "MetaModel{" +
                "type='" + type + '\'' +
                ", sourceMetadataClass=" + sourceMetadataClass +
                ", compileMetadataClass=" + compileMetadataClass +
                ", clientMetadataClass=" + clientMetadataClass +
                ", baseSourceMetadataClass=" + baseSourceMetadataClass +
                ", baseCompileMetadataClass=" + baseCompileMetadataClass +
                ", baseClientMetadataClass=" + baseClientMetadataClass +
                ", error=" + error +
                ", blank=" + blank +
                '}';
    }
}
