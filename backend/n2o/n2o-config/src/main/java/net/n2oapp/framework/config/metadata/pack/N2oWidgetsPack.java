package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.N2oButtonGeneratorFactory;
import net.n2oapp.framework.config.metadata.compile.tablesettings.*;
import net.n2oapp.framework.config.metadata.compile.toolbar.*;
import net.n2oapp.framework.config.metadata.compile.toolbar.table.*;
import net.n2oapp.framework.config.metadata.compile.widget.*;
import net.n2oapp.framework.config.metadata.compile.widget.table.DndColumnCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.table.FilterColumnCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.table.MultiColumnCompiler;
import net.n2oapp.framework.config.metadata.compile.widget.table.SimpleColumnCompiler;
import net.n2oapp.framework.config.metadata.merge.widget.N2oFormMerger;
import net.n2oapp.framework.config.metadata.merge.widget.N2oTableMerger;
import net.n2oapp.framework.config.metadata.merge.widget.N2oTilesMerger;
import net.n2oapp.framework.config.metadata.merge.widget.N2oWidgetMerger;

public class N2oWidgetsPack implements MetadataPack<N2oApplicationBuilder> {

    @Override
    public void build(N2oApplicationBuilder b) {
        b.packs(new N2oWidgetsIOPack(), new N2oWidgetsV5IOPack(), new N2oTableSettingsIOPack());
        b.compilers(new FormCompiler(),
                new ListWidgetCompiler(),
                new ToolbarCompiler(), new PerformButtonCompiler(), new SubmenuCompiler(),
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
                new CalendarCompiler(),
                new TilesCompiler(),
                new CardsCompiler());
        b.mergers(new N2oWidgetMerger(), new N2oFormMerger(), new N2oTableMerger(), new N2oTilesMerger());

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
                new TableWordWrapGenerator(),
                new TableExportGenerator(),
                new TableSettingsGenerator());
        b.binders(new WidgetBinder(),
                new TableBinder(),
                new ListWidgetBinder(),
                new FormBinder(),
                new TilesBinder(),
                new CardsBinder());
    }
}
