package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceMerger;

public interface BaseSourceMerger<S extends SourceMetadata> extends SourceMerger<S>, SourceClassAware {
}
