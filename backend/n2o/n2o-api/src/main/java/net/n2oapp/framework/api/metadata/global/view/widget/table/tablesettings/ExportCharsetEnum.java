package net.n2oapp.framework.api.metadata.global.view.widget.table.tablesettings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Кодировка экспортируемого файла
 */
@RequiredArgsConstructor
@Getter
public enum ExportCharsetEnum implements N2oEnum {
    UTF8("UTF-8"),
    CP1251("Cp1251");

    private static final Map<String, ExportCharsetEnum> BY_ID = new HashMap<>();

    static {
        for (ExportCharsetEnum s : values()) {
            BY_ID.put(s.id.toLowerCase(), s);
        }
    }

    private final String id;

    public static ExportCharsetEnum fromId(String id) {
        return BY_ID.get(id);
    }
}