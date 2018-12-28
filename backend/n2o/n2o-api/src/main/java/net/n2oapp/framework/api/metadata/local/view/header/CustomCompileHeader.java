package net.n2oapp.framework.api.metadata.local.view.header;

import net.n2oapp.framework.api.metadata.header.N2oCustomHeader;
import net.n2oapp.framework.api.metadata.local.N2oCompiler;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;

import java.util.Map;
import java.util.Set;

/**
 * User: operhod
 * Date: 20.02.14
 * Time: 11:37
 */
public class CustomCompileHeader extends CompiledHeader<N2oCustomHeader> {

    protected String src;

    public void setProperties(Map<String, Object> map) {
        this.properties = map;
    }

    @Override
    public void compile(N2oCustomHeader n2oHeader, N2oCompiler compiler, CompileContext context) {
        super.compile(n2oHeader, compiler, context);
        this.src = n2oHeader.getSrc();
    }

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public String getSrc() {
        return src;
    }

    @Override
    public void removePage(String pageId) {
    }

    @Override
    public Set<String> getAllPageIds() {
        return null;
    }


    @Override
    public Class<N2oCustomHeader> getSourceClass() {
        return N2oCustomHeader.class;
    }


}
