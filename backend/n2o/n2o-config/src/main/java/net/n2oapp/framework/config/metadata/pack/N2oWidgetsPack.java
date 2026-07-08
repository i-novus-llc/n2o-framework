package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.N2oButtonGeneratorFactory;
import net.n2oapp.framework.config.metadata.compile.tablesettings.*;
import net.n2oapp.framework.config.metadata.compile.toolbar.*;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.metadata.compile.widget.table.*;
import net.n2oapp.framework.config.metadata.merge.widget.*;

public class N2oWidgetsPack implements MetadataPack<N2oApplicationBuilder> {

    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oWidgetsV5IOPack(), new N2oTableSettingsIOPack());
        b.compilers(new FormCompiler(),
                new ListWidgetCompiler(),
                new MultiFormCompiler(),
                new ToolbarCompiler(), new PerformButtonCompiler(), new SubmenuCompiler(),
                new ClipboardButtonCompiler(),
                new ColumnsTableSettingCompiler(),
                new ExportTableSettingCompiler(),
                new FiltersTableSettingCompiler(),
                new RefreshTableSettingCompiler(),
                new ResizeTableSettingCompiler(),
                new WordWrapTableSettingCompiler(),
                new ResetTableSettingCompiler(),
                new TableCompiler(), new SimpleColumnCompiler(), new FilterColumnCompiler(), new MultiColumnCompiler(), new DndColumnCompiler(),
                new HtmlWidgetCompiler(),
                new CustomWidgetCompiler(),
                new TreeCompiler(),
                new ChartCompiler(),
                new TilesCompiler(),
                new CardsCompiler());
        b.mergers(new N2oWidgetMerger(), new N2oFormMerger(), new N2oTableMerger(),
                new N2oMultiFormMerger(),
                new N2oTilesMerger(), new N2oListMerger(), new N2oCardsMerger());

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
                new CloseGenerator());
        b.binders(new WidgetBinder(),
                new TableBinder(),
                new AbstractColumnBinder(),
                new DndColumnBinder(),
                new ListWidgetBinder(),
                new FormBinder(),
                new MultiFormBinder(),
                new TilesBinder(),
                new CardsBinder());
    }
}
