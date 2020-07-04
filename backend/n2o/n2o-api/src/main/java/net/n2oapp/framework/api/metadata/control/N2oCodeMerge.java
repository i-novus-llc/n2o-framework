package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.plain.CodeLanguageEnum;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;

@Getter
@Setter
public class N2oCodeMerge extends N2oStandardField {
    private MergeView mergeView;
    private Boolean showDifferences;
    private Boolean connectAlign;
    private Boolean collapseIdentical;
    private Boolean allowEditingOriginals;
    private Integer rows;
    private CodeLanguageEnum language;
    private String leftLabel;
    private String rightLabel;


    public enum MergeView implements IdAware {
        treeWay("three-way"),
        twoWay("two-way");

        private String value;

        MergeView(String value) {
            this.value = value;
        }

        @Override
        public String getId() {
            return value;
        }
    }
}
