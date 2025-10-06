package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.menu.*;

public class N2oMenuItemsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.compilers(
                new MenuItemCompiler(),
                new LinkMenuItemCompiler(),
                new GroupMenuItemCompiler(),
                new DropdownMenuItemCompiler(),
                new DividerMenuItemCompiler(),
                new ButtonMenuItemCompiler());
    }
}
