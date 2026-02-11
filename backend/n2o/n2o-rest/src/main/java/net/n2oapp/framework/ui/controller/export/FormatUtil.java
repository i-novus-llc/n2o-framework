package net.n2oapp.framework.ui.controller.export;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class FormatUtil {

    private FormatUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static final Pattern NON_DIGIT_PATTERN = Pattern.compile("\\D");
    private static final Pattern MASK_CHARS_PATTERN = Pattern.compile(".*[+()\\-].*");
    private static final Pattern DIGIT_PATTERN = Pattern.compile(".*\\d.*");
    private static final Pattern OPTIONAL_ZEROS_PATTERN = Pattern.compile("\\[0+]");

    public static String maskPassword(String value) {
        return "*".repeat(value.length());
    }

    public static String formatPhone(String value) {
        String digits = NON_DIGIT_PATTERN.matcher(value).replaceAll("");
        if (digits.length() < 10)
            return value;

        String phone = digits.length() > 10 ? digits.substring(digits.length() - 10) : digits;
        return String.format("+7 (%s) %s-%s-%s",
                phone.substring(0, 3),
                phone.substring(3, 6),
                phone.substring(6, 8),
                phone.substring(8, 10));
    }

    public static String formatSnils(String value) {
        String digits = NON_DIGIT_PATTERN.matcher(value).replaceAll("");
        if (digits.length() < 11)
            return value;

        return String.format("%s-%s-%s %s",
                digits.substring(0, 3),
                digits.substring(3, 6),
                digits.substring(6, 9),
                digits.substring(9, 11));
    }

    public static String formatNumber(Object value, String pattern) {
        if (value == null)
            return null;

        String digits;
        if (value instanceof Number number) {
            digits = String.valueOf(number.longValue());
        } else {
            digits = NON_DIGIT_PATTERN.matcher(value.toString()).replaceAll("");
        }

        if (MASK_CHARS_PATTERN.matcher(pattern).matches() && DIGIT_PATTERN.matcher(pattern).matches()) {
            return applyMask(digits, pattern);
        }

        try {
            Number num;
            if (value instanceof Number number) {
                num = number;
            } else {
                num = Double.parseDouble(value.toString());
            }

            String javaPattern = convertNumeralToJavaFormat(pattern);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
            symbols.setGroupingSeparator(' ');
            symbols.setDecimalSeparator(',');
            DecimalFormat df = new DecimalFormat(javaPattern, symbols);
            return df.format(num);
        } catch (NumberFormatException e) {
            return value.toString();
        }
    }

    public static String formatDate(Object value, String pattern) {
        if (value == null)
            return null;

        try {
            TemporalAccessor temporal;
            switch (value) {
                case LocalDateTime localDateTime -> temporal = localDateTime;
                case LocalDate localDate -> temporal = localDate;
                case ZonedDateTime zonedDateTime -> temporal = zonedDateTime;
                case Date date -> temporal = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                default -> {
                    String strValue = value.toString();
                    if (strValue.contains("T")) {
                        temporal = LocalDateTime.parse(strValue.substring(0, Math.min(strValue.length(), 19)));
                    } else {
                        temporal = LocalDate.parse(strValue);
                    }
                }
            }

            String javaPattern = convertMomentToJavaFormat(pattern);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(javaPattern);
            return formatter.format(temporal);
        } catch (Exception e) {
            return value.toString();
        }
    }

    private static String convertMomentToJavaFormat(String momentPattern) {
        return momentPattern
                .replace("YYYY", "yyyy")
                .replace("DD", "dd")
                .replace("ddd", "EEE")
                .replace("dddd", "EEEE")
                .replace("Z", "XXX");
    }

    private static String applyMask(String digits, String pattern) {
        StringBuilder result = new StringBuilder();
        int digitIndex = 0;

        for (char c : pattern.toCharArray()) {
            if (c == '0') {
                if (digitIndex < digits.length()) {
                    result.append(digits.charAt(digitIndex++));
                } else {
                    result.append('0');
                }
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // преобразование number аналогично логике numeral.js
    private static String convertNumeralToJavaFormat(String numeralPattern) {
        String result = numeralPattern;

        // "0,0" в numeral.js означает группировку тысяч
        if (result.contains("0,0")) {
            result = result.replace("0,0", "#,##0");
        } else {
            // Паттерны без группировки (0 0, 00 и т.д.) - убираем пробелы и лишние нули
            int dotIndex = result.indexOf('.');
            if (dotIndex > 0) {
                String intPart = result.substring(0, dotIndex);
                String decPart = result.substring(dotIndex);
                intPart = intPart.replace(" ", "").replaceAll("0+", "0");
                result = intPart + decPart;
            } else {
                result = result.replace(" ", "").replaceAll("0+", "0");
            }
        }

        if (result.contains("[")) {
            result = OPTIONAL_ZEROS_PATTERN.matcher(result).replaceAll("");
        }
        return result;
    }
}
