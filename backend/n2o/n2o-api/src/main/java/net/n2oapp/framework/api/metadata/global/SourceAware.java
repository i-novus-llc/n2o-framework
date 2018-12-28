package net.n2oapp.framework.api.metadata.global;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Метаданные, содержащие ссылку на свою реализацию (js, html, css, jsp)
 * @author iryabov
 * @since 11.12.2015
 */
@Deprecated
public interface SourceAware extends Serializable {
    String getSrc();
}
