package net.n2oapp.framework.api.metadata.aware;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import org.springframework.util.StringUtils;

/**
 * Тело элемента может быть вынесено во внешний файл
 */
public interface N2oFileAware {

    String getFile();

    void setFile(String file);

    /**
     * Возвращает содержимое файла, если задан атрибут file, иначе возвращает тело элемента.
     *
     * @param body Тело элемента (JS выражение, HTML и т.д.)
     * @param p    Процессор компиляции
     * @return Содержимое файла или тело элемента
     */
    default String resolveContent(String body, CompileProcessor p) {
        if (!StringUtils.hasText(getFile()))
            return body;
        String content = p.getExternalFile(getFile());
        return content != null ? content.replace("\r\n", "\n") : null;
    }
}