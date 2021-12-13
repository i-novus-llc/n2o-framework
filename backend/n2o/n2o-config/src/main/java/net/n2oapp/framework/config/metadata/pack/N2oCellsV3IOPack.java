package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.cell.v3.*;

/**
 * Набор считывателей ячеек версии 3.0
 */
public class N2oCellsV3IOPack implements MetadataPack<XmlIOBuilder> {
    @Override
    public void build(XmlIOBuilder b) {
        b.ios(new TextCellElementIOv3(),
                new CheckboxCellElementIOv3(),
                new LinkCellElementIOv3(),
                new ProgressBarCellElementIOv3(),
                new CheckboxCellElementIOv3(),
                new ImageCellElementIOv3(),
                new EditCellElementIOv3(),
                new BadgeCellElementIOv3(),
                new CustomCellElementIOv3(),
                new ToolbarCellElementIOv3(),
                new ListCellElementIOv3(),
                new IconCellElementIOv3(),
                new RatingCellElementIOv3(),
                new SwitchCellElementIOv3(),
                new RatingCellElementIOv3(),
                new TooltipListCellElementIOv3(),
                new FileUploadCellElementIOv3());
    }
}
