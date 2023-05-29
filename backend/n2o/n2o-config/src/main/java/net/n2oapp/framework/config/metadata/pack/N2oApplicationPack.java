package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.AnchorElementIOV1;
import net.n2oapp.framework.config.io.action.OpenPageElementIOV1;
import net.n2oapp.framework.config.io.application.ApplicationIOv2;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.io.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.io.menu.ExtraMenuIOv3;
import net.n2oapp.framework.config.io.menu.NavMenuIOv2;
import net.n2oapp.framework.config.io.menu.NavMenuIOv3;
import net.n2oapp.framework.config.metadata.compile.N2oComponentMerger;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationBinder;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationCompiler;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.N2oSidebarMerger;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarCompiler;
import net.n2oapp.framework.config.metadata.compile.header.SearchBarCompiler;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuCompiler;

/**
 * Набор для сборки приложения
 */
public class N2oApplicationPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.ios(
                new ApplicationIOv2(), new NavMenuIOv2(), new ExtraMenuIOv3(),
                new ApplicationIOv3(), new NavMenuIOv3(), new ExtraMenuIOv3(), new SidebarIOv3(),
                new OpenPageElementIOV1(), new AnchorElementIOV1()
        );
        b.compilers(
                new ApplicationCompiler(),
                new SimpleMenuCompiler(),
                new SearchBarCompiler(),
                new SidebarCompiler()
        );
        b.binders(new ApplicationBinder());
        b.mergers(new N2oSidebarMerger<>(), new N2oComponentMerger<>());
    }
}
