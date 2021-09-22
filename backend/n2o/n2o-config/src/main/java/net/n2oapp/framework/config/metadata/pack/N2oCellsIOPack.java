package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.config.io.widget.table.cell.*;

/**
 * Набор считывателей ячеек
 */
public class N2oCellsIOPack implements MetadataPack<ReadersBuilder> {
    @Override
    public void build(ReadersBuilder b) {
        b.ios(new TextCellElementIOv2(),
                new CheckboxCellElementIOv2(),
                new LinkCellElementIOv2(),
                new ProgressBarCellElementIOv2(),
                new CheckboxCellElementIOv2(),
                new ImageCellElementIOv2(),
                new EditCellElementIOv2(),
                new BadgeCellElementIOv2(),
                new CustomCellElementIOv2(),
                new ToolbarCellElementIOv2(),
                new ListCellElementIOv2(),
                new IconCellElementIOv2(),
                new RatingCellElementIOv2(),
                new SwitchCellElementIOv2(),
                new RatingCellElementIOv2(),
                new TooltipListCellElementIOv2(),
                new FileUploadCellElementIOv2());
    }
}
