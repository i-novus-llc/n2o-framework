package net.n2oapp.framework.export.format;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.export.streaming.N2oDataStreamingUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static net.n2oapp.properties.StaticProperties.isEnabled;

public class ExcelUtil {


    public static void writeAsExcel(Iterator<DataSet> iterator, List<N2oDataStreamingUtil.Field> fields, ServletOutputStream outputStream) {

        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        CellStyle dateStyle = createDataStyle(workbook);
        SXSSFSheet sheet = workbook.createSheet();
        int i = 0;

        if (isEnabled("n2o.ui.export.csv.header.enabled")) {
            Row row = sheet.createRow(i++);
            int j = 0;
            for (N2oDataStreamingUtil.Field field : fields) {
                Cell cell = row.createCell(j++);
                cell.setCellValue(field.getLabel());
            }
        }


        while (iterator.hasNext()) {
            DataSet data = iterator.next();
            Row row = sheet.createRow(i++);
            int j = 0;
            for (N2oDataStreamingUtil.Field field : fields) {
                Object value = data.get(field.getId());
                Cell cell = row.createCell(j++);
                if (value instanceof Number)
                    cell.setCellValue(((Number) value).doubleValue());
                else if (value instanceof Boolean)
                    cell.setCellValue((boolean) value);
                else if (value instanceof Date) {
                    cell.setCellValue((Date) value);
                    cell.setCellStyle(dateStyle);
                } else {
                    if (value == null)
                        value = "";
                    cell.setCellValue(value.toString());
                }
            }
        }
        int j = 0;
        while (j < fields.size()) {
            sheet.autoSizeColumn(j++);
        }
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle dateStyle = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd.MM.yyyy"));
        return dateStyle;
    }


}
