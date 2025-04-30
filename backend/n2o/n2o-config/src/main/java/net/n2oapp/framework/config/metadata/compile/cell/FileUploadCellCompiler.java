package net.n2oapp.framework.config.metadata.compile.cell;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oFileUploadCell;
import net.n2oapp.framework.api.metadata.meta.cell.FileUploadCell;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;

/**
 * Компиляция ячейки загрузки файлов
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
        build(cell, source, p, property("n2o.api.cell.file_upload.src"));

        cell.setUploadUrl(p.resolveJS(source.getUploadUrl()));
        cell.setDeleteUrl(p.resolveJS(source.getDeleteUrl()));

        cell.setMulti(castDefault(source.getMulti(),
                () -> p.resolve(property("n2o.api.cell.file_upload.multi"), Boolean.class)));
        cell.setAjax(castDefault(source.getAjax(),
                () -> p.resolve(property("n2o.api.cell.file_upload.ajax"), Boolean.class)));

        cell.setAccept(source.getAccept());
        cell.setShowSize(castDefault(source.getShowSize(),
                () -> p.resolve(property("n2o.api.cell.file_upload.show_size"), Boolean.class)));
        cell.setValueFieldId(castDefault(source.getValueFieldId(),
                () -> p.resolve(property("n2o.api.cell.file_upload.value_field_id"), String.class)));
        cell.setLabelFieldId(castDefault(source.getLabelFieldId(),
                () -> p.resolve(property("n2o.api.cell.file_upload.label_field_id"), String.class)));
        cell.setUrlFieldId(castDefault(source.getUrlFieldId(),
                () -> p.resolve(property("n2o.api.cell.file_upload.url_field_id"), String.class)));
        cell.setResponseFieldId(castDefault(source.getMessageFieldId(),
                () -> p.resolve(property("n2o.api.cell.file_upload.message_field_id"), String.class)));
        cell.setRequestParam(castDefault(source.getRequestParam(),
                () -> p.resolve(property("n2o.api.cell.file_upload.request_param"), String.class)));
        cell.setUploadIcon(source.getUploadIcon());
        cell.setDeleteIcon(source.getDeleteIcon());
        cell.setLabel(source.getLabel());

        return cell;
    }
}
