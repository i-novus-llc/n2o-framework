package net.n2oapp.framework.ui.controller.export.format;

import net.n2oapp.criteria.dataset.DataSet;

import java.util.List;
import java.util.Map;

/**
 * Генератор файлов по формату
 */
public interface FileGenerator {

    byte[] createFile(String fileName, String fileDir, String charset, List<DataSet> data, Map<String, String> headers);

    String getFormat();

    String getContentType();
}
