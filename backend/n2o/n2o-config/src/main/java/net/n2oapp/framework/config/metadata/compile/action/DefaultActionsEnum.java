package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesModeEnum;

import java.util.HashMap;
import java.util.Map;

public enum DefaultActionsEnum {
    CREATE("create", false, false, true, "n2o.create", true, "fa fa-plus", "n2o.save", "n2o.page.creating", DefaultValuesModeEnum.DEFAULTS),
    UPDATE("update", true, true, "n2o.update", true, "fa fa-pencil", "n2o.save", "n2o.page.updating", DefaultValuesModeEnum.QUERY),
    DELETE("delete", true, false, "n2o.delete", false, "fa fa-trash");

    private static final Map<String, DefaultActionsEnum> actionsByNameMap = new HashMap<>();

    static {
        for (DefaultActionsEnum da : DefaultActionsEnum.values())
            actionsByNameMap.put(da.name(), da);
    }

    private final String id;
    private boolean context;
    private boolean defaultAction;
    private boolean primary;
    private String label;
    private boolean modal;
    private String icon;
    private String formSubmitLabel;
    private String pageName;
    private DefaultValuesModeEnum mode;

    DefaultActionsEnum(String id, boolean context, boolean defaultAction, String label, boolean modal,
                       String icon, String formSubmitLabel, String pageName, DefaultValuesModeEnum upload) {
        this.id = id;
        this.context = context;
        this.defaultAction = defaultAction;
        this.label = label;
        this.modal = modal;
        this.icon = icon;
        this.formSubmitLabel = formSubmitLabel;
        this.pageName = pageName;
        this.mode = upload;
    }

    DefaultActionsEnum(String id, boolean context, boolean defaultAction, boolean primary, String label, boolean modal,
                       String icon, String formSubmitLabel, String pageName, DefaultValuesModeEnum upload) {
        this.id = id;
        this.context = context;
        this.defaultAction = defaultAction;
        this.primary = primary;
        this.label = label;
        this.modal = modal;
        this.icon = icon;
        this.formSubmitLabel = formSubmitLabel;
        this.pageName = pageName;
        this.mode = upload;
    }

    DefaultActionsEnum(String id, boolean context, boolean defaultAction, String label, boolean modal, String icon) {
        this.id = id;
        this.context = context;
        this.defaultAction = defaultAction;
        this.label = label;
        this.modal = modal;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }
    public static DefaultActionsEnum get(String name) {
        return actionsByNameMap.get(name);
    }

    public String getIcon() {
        return icon;
    }

    public boolean isContext() {
        return context;
    }

    public boolean isDefaultAction() {
        return defaultAction;
    }

    public String getLabel() {
        return label;
    }

    public boolean isModal() {
        return modal;
    }

    public String getFormSubmitLabel() {
        return formSubmitLabel;
    }

    public String getPageName() {
        return pageName;
    }

    public boolean isPrimary() {
        return primary;
    }

    public DefaultValuesModeEnum getMode() {
        return mode;
    }
}