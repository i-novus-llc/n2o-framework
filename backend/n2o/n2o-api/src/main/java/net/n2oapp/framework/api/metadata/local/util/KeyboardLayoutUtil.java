package net.n2oapp.framework.api.metadata.local.util;

/**
 * User: iryabov
 * Date: 14.01.14
 * Time: 17:43
 */
public class KeyboardLayoutUtil {
    private static final String[] EN = new String[]{"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "[", "]", "a", "s", "d", "f", "g", "h", "j", "k", "l", ";", "'", "z", "x", "c", "v", "b", "n", "m", ",", ".", "`"};
    private static final String[] RU = new String[]{"й", "ц", "у", "к", "е", "н", "г", "ш", "щ", "з", "х", "ъ", "ф", "ы", "в", "а", "п", "р", "о", "л", "д", "ж", "э", "я", "ч", "с", "м", "и", "т", "ь", "б", "ю", "ё"};

    public static String getRuToEn(String ru) {
        for (int k = 0; k < RU.length; k++) {
            if (RU[k].equals(ru)) {
                return EN[k];
            }
        }
        return ru;
    }

    public static String getEnToRu(String en) {
        for (int k = 0; k < EN.length; k++) {
            if (EN[k].equals(en)) {
                return RU[k];
            }
        }
        return en;
    }
}
