package net.n2oapp.framework.engine.data.normalize;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Optional;

@Normalizer
public class DateFormatNormalizer {

    private static final String DEFAULT_OUTPUT_DATE_FORMAT = "dd.MM.yyyy";
    private static final DateTimeFormatter DEFAULT_FORMATTER;

    static {
        DEFAULT_FORMATTER = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE)
                .optionalStart()
                .optionalStart()
                .appendLiteral(' ')
                .optionalEnd()
                .optionalStart()
                .appendLiteral('T')
                .optionalEnd()
                .appendOptional(DateTimeFormatter.ISO_TIME)
                .toFormatter();
    }

    private DateFormatNormalizer() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Преобразование даты из формата ISO в формат dd.MM.yyyy
     *
     * @param dateStr Строковое представление даты(или даты и времени) в формате ISO
     * @return Строковое представление даты в формате dd.MM.yyyy
     */
    public static String date(String dateStr) {
        return dateWithOutput(dateStr, DEFAULT_OUTPUT_DATE_FORMAT);
    }

    /**
     * Преобразование даты из указанного формата в формат dd.MM.yyyy
     *
     * @param dateStr     Строковое представление даты(или даты и времени) в входном формате
     * @param inputFormat Входной формат даты
     * @return Строковое представление даты в формате dd.MM.yyyy
     */
    public static String dateWithInput(String dateStr, String inputFormat) {
        return dateWithInputAndOutput(dateStr, inputFormat, DEFAULT_OUTPUT_DATE_FORMAT);
    }

    /**
     * Преобразование даты из формата ISO в указанный формат
     *
     * @param dateStr      Строковое представление даты(или даты и времени) в формате ISO
     * @param outputFormat Выходной формат даты
     * @return Строковое представление даты в выходном формате
     */
    public static String dateWithOutput(String dateStr, String outputFormat) {
        if (dateStr == null) {
            return null;
        }

        if (isValidDate(dateStr, DEFAULT_FORMATTER)) {
            if (hasTime(dateStr, DEFAULT_FORMATTER))
                return formatDate(LocalDateTime.from(DEFAULT_FORMATTER.parse(dateStr)), outputFormat);
            return formatDate(LocalDate.from(DEFAULT_FORMATTER.parse(dateStr)), outputFormat);
        }
        return dateStr;
    }

    /**
     * Преобразование даты из входного формата в выходной
     *
     * @param dateStr      Строковое представление даты(или даты и времени) в входном формате
     * @param inputFormat  Входной формат даты
     * @param outputFormat Выходной формат даты
     * @return Строковое представление даты в выходном формате
     */
    public static String dateWithInputAndOutput(String dateStr, String inputFormat, String outputFormat) {
        if (dateStr == null) {
            return null;
        }
        Optional<String> optional = formatWithLocalDate(dateStr, inputFormat, outputFormat);
        return optional.orElse(dateStr);
    }

    /**
     * Преобразование двух дат к интервальному виду
     * <p>
     * Пример:
     * <p>
     * period("12.09.2022", "13.09.2022") = "12.09.2022 - 13.09.2022"
     * period("12.09.2022", null)         = "12.09.2022 - "
     * period(null, "13.09.2022")         = " - 13.09.2022"
     * period(null, null)                 = " - "
     *
     * @param startDate Начальное значение даты
     * @param endDate   Конечное значение даты
     * @return Строка интервала дат
     */
    public static String period(String startDate, String endDate) {
        return StringUtils.join(Arrays.asList(date(startDate), date(endDate)), " - ");
    }

    private static Optional<String> formatWithLocalDate(String dateStr, String inputFormat, String outputFormat) {
        if (isValidDate(dateStr, DateTimeFormatter.ofPattern(inputFormat))) {
            if (hasTime(dateStr, DateTimeFormatter.ofPattern(inputFormat)))
                return Optional.of(formatDate(LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(inputFormat)), outputFormat));
            return Optional.of(formatDate(LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(inputFormat)), outputFormat));
        }
        return Optional.empty();
    }

    private static boolean isValidDate(String dateStr, DateTimeFormatter formatter) {
        try {
            formatter.parse(dateStr);
        } catch (DateTimeParseException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    private static boolean hasTime(String dateStr, DateTimeFormatter pattern) {
        try {
            LocalDateTime.parse(dateStr, pattern);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static String formatDate(LocalDate date, String outputFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outputFormat);
        return date.format(formatter);
    }

    private static String formatDate(LocalDateTime date, String outputFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outputFormat);
        return date.format(formatter);
    }
}
