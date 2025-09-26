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
import java.util.Set;

@Component
public class XlsxFileGenerator implements FileGenerator {

    private static final String FILE_FORMAT = "xlsx";
    private static final int MIN_COLUMN_WIDTH = 20;

    @Override
    public byte[] createFile(String fileName, String fileDir, String charset, List<DataSet> data, List<String> headers) {
        byte[] fileBytes = null;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers.get(i));
                cell.setCellStyle(headerStyle);

                // задаём ширину колонки по длине текста заголовка + 2 символа для отступа
                int width = (Math.max(MIN_COLUMN_WIDTH, headers.get(i).length()) + 2) * 256;
                sheet.setColumnWidth(i, width);
            }

            if (!data.isEmpty()) {
                Set<String> keys = data.getFirst().flatKeySet();
                int rowNum = 1;
                for (DataSet dataSet : data) {
                    Row row = sheet.createRow(rowNum++);
                    int colNum = 0;
                    for (String key : keys) {
                        Cell cell = row.createCell(colNum++);
                        setCellValue(cell, dataSet.get(key));
                    }
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
