package net.n2oapp.framework.access.metadata.pack;

import net.n2oapp.framework.access.integration.metadata.transform.*;
import net.n2oapp.framework.access.integration.metadata.transform.action.InvokeActionAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.action.OpenPageAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.action.ShowModalAccessTransformer;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;

public class AccessTransformersPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.transformers(
                new HeaderAccessTransformer(),
                new ObjectAccessTransformer(),
                new PageAccessTransformer(),
                new QueryAccessTransformer(),
                new ToolbarAccessTransformer(),
                new ToolbarCellAccessTransformer(),
                new WidgetAccessTransformer(),
                new InvokeActionAccessTransformer(),
                new OpenPageAccessTransformer(),
                new ShowModalAccessTransformer(),
                new TableAccessTransformer(),
                new ListWidgetAccessTransformer(),
                new StandardFieldAccessTransformer(),
                new ButtonFieldAccessTransformer());
    }
}
