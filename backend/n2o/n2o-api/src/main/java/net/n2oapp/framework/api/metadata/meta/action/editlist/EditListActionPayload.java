package net.n2oapp.framework.api.metadata.meta.action.editlist;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.meta.action.ActionPayload;

/**
 * Полезная нагрузка действия редактирования записи списка
 */
@Getter
@Setter
public class EditListActionPayload implements ActionPayload {
    @JsonProperty
    private ListOperationTypeEnum operation;
    @JsonProperty
    private String primaryKey;
    @JsonProperty
    private EditInfo list;
    @JsonProperty
    private EditInfo item;

    @Getter
    @Setter
    public static class EditInfo implements Compiled {
        @JsonProperty
        private String datasource;
        @JsonProperty
        private ReduxModelEnum model;
        @JsonProperty
        private String field;

        public EditInfo(String datasource, ReduxModelEnum model, String field) {
            this.datasource = datasource;
            this.model = model;
            this.field = field;
        }
    }
}
