package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oFileUploadCell;
import net.n2oapp.framework.api.metadata.meta.cell.FileUploadCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция ячейки загрузки файла
 */
@Component
public class FileUploadCellCompiler  extends AbstractCellCompiler<FileUploadCell, N2oFileUploadCell> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oFileUploadCell.class;
    }

    @Override
    public FileUploadCell compile(N2oFileUploadCell source, CompileContext<?, ?> context, CompileProcessor p) {
        FileUploadCell cell = new FileUploadCell();
        build(cell, source, context, p, property("n2o.api.cell.file_upload.src"));

        cell.setUploadUrl(p.resolveJS(source.getUploadUrl()));
        cell.setDeleteUrl(p.resolveJS(source.getDeleteUrl()));
        cell.setAjax(p.cast(source.getAjax(), true));
        cell.setMulti(p.cast(source.getMulti(), false));
        cell.setAccept(source.getAccept());
        cell.setShowSize(p.cast(source.getShowSize(),
                p.resolve(property("n2o.api.cell.file_upload.show_size"), Boolean.class)));
        cell.setValueFieldId(p.cast(source.getValueFieldId(),
                p.resolve(property("n2o.api.cell.file_upload.value_field_id"), String.class)));
        cell.setLabelFieldId(p.cast(source.getLabelFieldId(),
                p.resolve(property("n2o.api.cell.file_upload.label_field_id"), String.class)));
        cell.setUrlFieldId(p.cast(source.getUrlFieldId(),
                p.resolve(property("n2o.api.cell.file_upload.url_field_id"), String.class)));
        cell.setResponseFieldId(p.cast(source.getMessageFieldId(),
                p.resolve(property("n2o.api.cell.file_upload.response_field_id"), String.class)));
        cell.setRequestParam(p.cast(source.getRequestParam(), "file"));
        cell.setUploadIcon(source.getUploadIcon());
        cell.setDeleteIcon(source.getDeleteIcon());
        cell.setLabel(source.getLabel());

        return cell;
    }
}
