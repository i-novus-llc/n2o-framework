package net.n2oapp.framework.ui.controller.export.format;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportRequest;

import java.util.List;

/**
 * Генератор файлов по формату
 */
public interface FileGenerator {

    byte[] createFile(String fileName, String fileDir, String charset, List<DataSet> data, List<ExportRequest.ExportField> headers);

    String getFormat();

    String getContentType();
}
