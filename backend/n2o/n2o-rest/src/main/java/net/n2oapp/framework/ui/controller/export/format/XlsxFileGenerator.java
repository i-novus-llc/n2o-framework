package net.n2oapp.framework.ui.controller.export.format;

import net.n2oapp.criteria.dataset.DataSet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Component
public class XlsxFileGenerator implements FileGenerator {

    private static final String FILE_FORMAT = "xlsx";
    private static final int MIN_COLUMN_WIDTH = 20;

    @Override
    public byte[] createFile(String fileName, String fileDir, String charset, List<DataSet> data, Map<String, String> headers) {
        byte[] fileBytes = null;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            CellStyle headerStyle = createHeaderStyle(workbook);

            // Заголовки и ширина колонок по headers (порядок соответствует LinkedHashMap)
            Row headerRow = sheet.createRow(0);
            int colIndex = 0;
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                String headerTitle = entry.getValue();
                Cell cell = headerRow.createCell(colIndex);
                cell.setCellValue(headerTitle);
                cell.setCellStyle(headerStyle);

                int width = (Math.max(MIN_COLUMN_WIDTH, headerTitle != null ? headerTitle.length() : 0) + 2) * 256;
                sheet.setColumnWidth(colIndex, width);
                colIndex++;
            }

            // Данные построчно по ключам из headers
            int rowNum = 1;
            for (DataSet rowData : data) {
                Row row = sheet.createRow(rowNum++);
                int c = 0;
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    Cell cell = row.createCell(c++);
                    setCellValue(cell, rowData.get(entry.getKey()));
                }
            }

            String fullFileName = fileDir + File.separator + fileName + "." + FILE_FORMAT;
            try (FileOutputStream fileOut = new FileOutputStream(fullFileName)) {
                workbook.write(fileOut);
            }

            fileBytes = Files.readAllBytes(Path.of(fullFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileBytes;
    }

    @Override
    public String getFormat() {
        return FILE_FORMAT;
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private void setCellValue(Cell cell, Object value) {
        switch (value) {
            case null -> cell.setCellValue("");
            case String s -> cell.setCellValue(s);
            case Number number -> cell.setCellValue(number.doubleValue());
            default -> cell.setCellValue(value.toString());
        }
    }
}
