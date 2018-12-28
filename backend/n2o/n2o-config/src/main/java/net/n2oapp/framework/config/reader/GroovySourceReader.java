package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.config.register.GroovyInfo;
import net.n2oapp.framework.config.util.FileSystemUtil;

import static net.n2oapp.framework.config.groovy.GroovyScriptProcessor.getFromScript;

/**
 * Считыватель метаданных из groovy скриптов
 */
public class GroovySourceReader implements SourceLoader<GroovyInfo> {

    public GroovySourceReader() {
    }

    @Override
    public <S extends SourceMetadata> S load(GroovyInfo info, String params) {
        S n2o = getFromScript(getScript(info), (Class<S>)info.getBaseSourceClass());
        n2o.setId(info.getId());
        return n2o;
    }

    private String getScript(GroovyInfo info) {
        return FileSystemUtil.getContentByUri(info.getUri());
    }

}
