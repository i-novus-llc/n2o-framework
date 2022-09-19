package net.n2oapp.framework.engine.data.normalize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNullElse;

/**
 * Утилитный класс для функций нормализации данных
 */
@Normalizer
public class NormalizeUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private NormalizeUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Преобразование строки json в мапу
     * Пример:
     *
     *  входные данные:
     *  "{ \"id\": 1,\"name\": \"test\" }"
     *
     *  выходные данные:
     *  {
     *     "id": 1,
     *     "name": "test"
     *  }
     *
     * @param json строка json
     * @return сконвертированная по строке мапа
     */
    public static DataSet jsonToMap(String json) {
        try {
            return new DataSet(objectMapper.readValue(json, new TypeReference<>() {
            }));
        } catch (JsonProcessingException e) {
            throw new N2oException("Unable to apply function \"#jsonToMap\"", e);
        }
    }

    /**
     * Преобразование объекта в строковое json-представление
     *
     * Пример:
     *
     *  входные данные:
     *  {
     *     "id": 1,
     *     "name": "test"
     *  }
     *
     *  выходные данные:
     *  "{ \"id\": 1,\"name\": \"test\" }"
     *
     * @param map объект
     * @return json-представление объекта
     */
    public static String mapToJson(Object map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new N2oException("Unable to apply function \"#mapToJson\"", e);
        }
    }

    /**
     * Преобразование строки из UTF-16 в base64
     *
     * Пример:
     *
     *  входные данные: "test"
     *  выходные данные: "dGVzdA=="
     *
     * @param text преобразуемая строка
     * @return строка в кодировке base64
     */
    public static String encodeToBase64(String text) {
        return Base64.getUrlEncoder().encodeToString(text.getBytes());
    }

    /**
     * Преобразование строки из base64 в UTF-16
     *
     * Пример:
     *
     *  входные данные: "dGVzdA=="
     *  выходные данные: "test"
     *
     * @param base64 преобразуемая строка
     * @return строка в кодировке UTF-16
     */
    public static String decodeFromBase64(String base64) {
        return new String(Base64.getUrlDecoder().decode(base64));
    }

    /**
     * Форматирование значения по маске
     *
     * Пример:
     *
     *  входные данные: 11122233344, "###-###-### ##"
     *  выходные данные: "111-222-333 44"
     *
     * @param value форматируемое значение
     * @param mask маска
     * @return форматированная по маске строка
     */
    public static String formatByMask(Object value, String mask) {
        try {
            MaskFormatter formatter =  new MaskFormatter(mask);
            formatter.setValueContainsLiteralCharacters(false);
            return formatter.valueToString(value);
        } catch (ParseException e) {
            throw new N2oException(String.format("Unable to format %s by mask %s", value, mask), e);
        }
    }

    /**
     * Форматирование ФИО без сокращений
     * В выходной строке сохраняется порядок переданных аргументов
     *
     * Пример 1:
     *
     *  входные данные: "Лев", "Николаевич", "Толстой"
     *  выходные данные: "Лев Николаевич Толстой"
     *
     * Пример 2:
     *
     *  входные данные: "Маркс", "Карл"
     *  выходные данные: "Маркс Карл"
     *
     * @param names Список ФИО (допускается ФИ, ИО и т.д.)
     * @return форматированная строка
     */
    public static String formatFullName(String... names) {
        return Stream.of(names).map(name -> requireNonNullElse(name, "")).collect(Collectors.joining(" "));
    }

    /**
     * Форматирование ФИО с использованием инициалов
     * В выходной строке сохраняется порядок переданных аргументов
     *
     * Пример 1:
     *
     *  входные данные: "Толстой", "Лев", "Николаевич",
     *  выходные данные: "Толстой Л.Н."
     *
     * Пример 2:
     *
     *  входные данные: "Маркс", "Карл"
     *  выходные данные: "Маркс К."
     *
     * @param names Список ФИО
     * @return форматированная строка
     */
    public static String formatNameWithInitials(String... names) {
        return names[0] + (names[1] != null ? String.format(" %s.", names[1].toCharArray()[0]) : "") +
                (names[2] != null ? String.format("%s.", names[2].toCharArray()[0]) : "");
    }
}
