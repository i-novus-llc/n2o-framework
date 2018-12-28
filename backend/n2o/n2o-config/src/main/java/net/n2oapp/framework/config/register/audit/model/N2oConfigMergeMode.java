package net.n2oapp.framework.config.register.audit.model;

/**
 * @author dfirstov
 * @since 22.09.2015
 */
public enum N2oConfigMergeMode {
    MANUAL("manual"),
    OURS ("ours"),
    THEIRS ("theirs"),
    MERGE_OURS("merge_ours"),
    MERGE_THEIRS ("merge_theirs");

    N2oConfigMergeMode(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return this.value;
    }

}