package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.CssClassAware;
import net.n2oapp.framework.api.metadata.aware.NameAware;
import net.n2oapp.framework.api.metadata.aware.SrcAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.compile.building.BuildProcessor;
import net.n2oapp.framework.api.metadata.compile.building.CompileBuilder;

public class BaseCompiler<D extends Compiled, S> implements CompileBuilder<D, S> {

    @Override
    public void build(BuildProcessor<D, S> p) {
        p.optionalCast(IdAware.class).get(IdAware::getId).set(IdAware::setId);
        p.optionalCast(NameAware.class).get(NameAware::getName).set(NameAware::setName);
        p.optionalCast(SrcAware.class).get(SrcAware::getSrc).set(SrcAware::setSrc);
        p.optionalCast(CssClassAware.class).get(CssClassAware::getCssClass).set(CssClassAware::setCssClass);
    }


}
