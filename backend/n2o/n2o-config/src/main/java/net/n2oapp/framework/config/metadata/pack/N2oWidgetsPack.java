package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.N2oButtonGeneratorFactory;
import net.n2oapp.framework.config.metadata.compile.toolbar.*;
import net.n2oapp.framework.config.metadata.compile.toolbar.table.*;
import net.n2oapp.framework.config.metadata.compile.widget.*;

public class N2oWidgetsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oWidgetsIOPack());
        b.compilers(new FormCompiler(),
                new ListWidgetCompiler(),
                new ToolbarCompiler(),
                new TableCompiler(),
                new HtmlWidgetCompiler(),
                new CustomWidgetCompiler(),
                new TreeCompiler());
        b.mergers(new N2oWidgetMerger(), new N2oFormMerger());

        CrudGenerator crudGenerator = new CrudGenerator();
        N2oButtonGeneratorFactory buttonGeneratorFactory = new N2oButtonGeneratorFactory();
        buttonGeneratorFactory.add(new CreateGenerator(), new UpdateGenerator(), new DeleteGenerator());
        buttonGeneratorFactory.setEnvironment(b.getEnvironment());
        crudGenerator.setButtonGeneratorFactory(buttonGeneratorFactory);

        b.generators(crudGenerator,
                new CreateGenerator(),
                new UpdateGenerator(),
                new DeleteGenerator(),
                new SubmitGenerator(),
                new CloseGenerator(),
                new TableColumnsGenerator(),
                new TableFiltersGenerator(),
                new TableRefreshGenerator(),
                new TableResizeGenerator(),
                new TableSettingsGenerator());
        b.binders(new WidgetBinder(), new TableBinder());


    }
}
