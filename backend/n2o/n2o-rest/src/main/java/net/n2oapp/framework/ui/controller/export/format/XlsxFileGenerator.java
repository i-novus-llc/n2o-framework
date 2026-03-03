package net.n2oapp.framework.ui.controller.export.format;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.rest.ExportRequest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.n2oapp.framework.ui.controller.export.FormatUtil.applyFormat;

@Component
public class XlsxFileGenerator implements FileGenerator {

    private static final String FILE_FORMAT = "xlsx";
    private static final int MIN_COLUMN_WIDTH = 20;

    @Override
    public byte[] createFile(String fileName, String fileDir, String charset,
                             List<DataSet> data,
                             List<ExportRequest.ExportField> headers) {
        byte[] fileBytes = null;

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet();
            CellStyle headerStyle = createHeaderStyle(workbook);
            Map<Integer, ColumnStyle> columnStyles = createColumnStyles(workbook, headers);

            // Заголовки и ширина колонок по headers (порядок соответствует LinkedHashMap)
            Row headerRow = sheet.createRow(0);
            int colIndex = 0;
            for (ExportRequest.ExportField entry : headers) {
                String headerTitle = entry.getTitle();
                Cell cell = headerRow.createCell(colIndex);
                cell.setCellValue(headerTitle);
                cell.setCellStyle(headerStyle);

                int width = (Math.max(MIN_COLUMN_WIDTH, headerTitle != null ? headerTitle.length() : 0) + 2) * 256;
                sheet.setColumnWidth(colIndex, width);
                colIndex++;
            }

            // Данные построчно по ключам из headers
            int rowNum = 1;
            for (DataSet datum : data) {
                Row row = sheet.createRow(rowNum++);
                int c = 0;
                for (ExportRequest.ExportField entry : headers) {
                    Cell cell = row.createCell(c);
                    if (entry.getFormat() != null) {
                        String formatLower = entry.getFormat().toLowerCase().trim();
                        if (formatLower.startsWith("date") || formatLower.startsWith("number")) {
                            setCellWithStyle(cell, datum.get(entry.getId()), columnStyles.get(c));
                        } else {
                            setCellValue(cell, applyFormat(datum.get(entry.getId()), entry.getFormat()));
                        }
                    } else {
                        setCellValue(cell, datum.get(entry.getId()));
                    }
                    c++;
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

    /**
     * Создаёт стили для колонок с датами и числами на основе формата из headers.
     *
     * @return Map с индексом колонки и соответствующим стилем
     */
    private Map<Integer, ColumnStyle> createColumnStyles(Workbook workbook, List<ExportRequest.ExportField> headers) {
        Map<Integer, ColumnStyle> styles = new HashMap<>();
        CreationHelper createHelper = workbook.getCreationHelper();

        int colIndex = 0;
        for (ExportRequest.ExportField header : headers) {
            String format = header.getFormat();
            if (format != null) {
                String formatLower = format.toLowerCase().trim();
                CellStyle style = workbook.createCellStyle();
                if (formatLower.startsWith("date ")) {
                    style.setDataFormat(createHelper.createDataFormat().getFormat(getExcelDateFormat(format.substring(5).trim())));
                    styles.put(colIndex, new ColumnStyle(ColumnType.DATE, style));
                } else if (formatLower.startsWith("number ")) {
                    style.setDataFormat(createHelper.createDataFormat().getFormat(convertToExcelNumberFormat(format.substring(7).trim())));
                    styles.put(colIndex, new ColumnStyle(ColumnType.NUMBER, style));
                }
            }
            colIndex++;
        }
        return styles;
    }

    /**
     * Конвертирует формат даты moment.js в формат Excel.
     *
     * @param format формат даты в стиле moment.js (например, "DD.MM.YYYY HH:mm:ss")
     * @return формат даты для Excel (например, "dd.mm.yyyy hh:mm:ss")
     */
    private String getExcelDateFormat(String format) {
        if (format == null || format.isEmpty()) {
            return "dd.mm.yyyy";
        }

        String result = format;

        // Год: YYYY -> yyyy, YY -> yy
        result = result.replace("YYYY", "yyyy");
        result = result.replace("YY", "yy");

        // День: DD -> dd, D -> d (порядок важен - сначала DD)
        result = result.replace("DD", "dd");
        result = result.replace("D", "d");

        // Месяц: MM -> mm, M -> m (порядок важен - сначала MM)
        // Заменяем временно на плейсхолдер, чтобы не конфликтовать с минутами
        result = result.replace("MM", "\u0001\u0001");
        result = result.replace("M", "\u0001");

        // Часы: HH -> hh, H -> h (24-часовой формат)
        result = result.replace("HH", "hh");
        result = result.replace("H", "h");

        // Минуты и секунды: mm, ss - остаются без изменений в Excel
        // но нужно убедиться, что mm для минут не конфликтует с месяцем

        // Возвращаем месяц из плейсхолдера
        result = result.replace("\u0001\u0001", "mm");
        result = result.replace("\u0001", "m");

        // Миллисекунды: SSS -> 000
        result = result.replace("SSS", "000");
        result = result.replace("SS", "00");
        result = result.replace("S", "0");

        return result;
    }

    /**
     * Конвертирует numeral.js формат в Excel формат.
     */
    private String convertToExcelNumberFormat(String pattern) {
        if (pattern == null || pattern.isEmpty()) {
            return "#,##0.00";
        }

        String result = pattern;
        // Конвертация numeral.js формата в Excel формат
        result = result.replace("0,0", "#,##0");
        // Убираем опциональные нули [0]
        result = result.replaceAll("\\[0+]", "");

        return result;
    }

    private void setCellValue(Cell cell, Object value) {
        switch (value) {
            case null -> cell.setCellValue("");
            case String s -> cell.setCellValue(s);
            case Number number -> cell.setCellValue(number.doubleValue());
            default -> cell.setCellValue(value.toString());
        }
    }

    private void setCellWithStyle(Cell cell, Object value, ColumnStyle columnStyle) {
        if (value == null) {
            cell.setCellValue("");
            return;
        }

        try {
            switch (columnStyle.type) {
                case DATE -> {
                    Date date = convertToDate(value);
                    if (date != null) {
                        cell.setCellValue(date);
                        cell.setCellStyle(columnStyle.style);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
                case NUMBER -> {
                    Double number = convertToNumber(value);
                    if (number != null) {
                        cell.setCellValue(number);
                        cell.setCellStyle(columnStyle.style);
                    } else {
                        cell.setCellValue(value.toString());
                    }
                }
            }
        } catch (Exception e) {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * Конвертирует значение в число.
     */
    private Double convertToNumber(Object value) {
        return switch (value) {
            case Number number -> number.doubleValue();
            case String str -> {
                try {
                    yield Double.parseDouble(str.replaceAll("[^\\d.,-]", "").replace(",", "."));
                } catch (NumberFormatException e) {
                    yield null;
                }
            }
            default -> null;
        };
    }

    /**
     * Конвертирует различные типы дат в java.util.Date для Excel.
     */
    private Date convertToDate(Object value) {
        return switch (value) {
            case Date date -> date;
            case LocalDateTime localDateTime -> Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            case LocalDate localDate -> Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            case ZonedDateTime zonedDateTime -> Date.from(zonedDateTime.toInstant());
            case Instant instant -> Date.from(instant);
            case String str -> parseStringToDate(str);
            default -> null;
        };
    }

    private Date parseStringToDate(String value) {
        try {
            if (value.contains("T")) {
                LocalDateTime dateTime = LocalDateTime.parse(value.substring(0, Math.min(value.length(), 19)));
                return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            } else {
                LocalDate date = LocalDate.parse(value);
                return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            }
        } catch (Exception e) {
            return null;
        }
    }

    private enum ColumnType {
        DATE,
        NUMBER
    }

    private record ColumnStyle(ColumnType type, CellStyle style) {}
}
