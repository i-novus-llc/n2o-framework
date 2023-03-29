package net.n2oapp.framework.sandbox.templates;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Модель, содержащая совпадения в проектах по входной строке
 */
@Getter
@Setter
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.ANY)//TODO убрать после слития https://jira.i-novus.ru/browse/NNO-6485
public class SearchProjectModel {
    private String projectId;
    private List<Item> items;

    public SearchProjectModel(String projectId, List<Item> items) {
        this.projectId = projectId;
        this.items = items;
    }


    @Getter
    @Setter
    @JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.ANY)//TODO убрать после слития https://jira.i-novus.ru/browse/NNO-6485
    public static class Item {
        private String filename;
        private String line;
        private Integer lineNumber;

        public Item(String filename, String line, Integer lineNumber) {
            this.filename = filename;
            this.line = line;
            this.lineNumber = lineNumber;
        }
    }
}
