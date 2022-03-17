package net.n2oapp.framework.sandbox.server.editor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Категория
 */
@Getter
@Setter
public class CategoryModel {
    @JsonProperty
    private String name;

    @JsonProperty
    private List<SectionModel> sections;
}
