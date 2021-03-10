package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.event.action.UploadType;

import java.util.HashMap;
import java.util.Map;

public enum DefaultActions {
    create(false, false, true, "n2o.create", true, "fa fa-plus", "n2o.save", "n2o.page.creating", UploadType.defaults),
    update(true, true, "n2o.update", true, "fa fa-pencil", "n2o.save", "n2o.page.updating", UploadType.query),
    delete(true, false, "n2o.delete", false, "fa fa-trash");

    private static final Map<String, DefaultActions> actionsByNameMap = new HashMap<>();

    static {
        for (DefaultActions da : DefaultActions.values())
            actionsByNameMap.put(da.name(), da);
    }

    private boolean context;
    private boolean defaultAction;
    private boolean primary;
    private String label;
    private boolean modal;
    private String icon;
    private String formSubmitLabel;
    private String pageName;
    private UploadType upload;

    DefaultActions(boolean context, boolean defaultAction, String label, boolean modal,
                   String icon, String formSubmitLabel, String pageName, UploadType upload) {
        this.context = context;
        this.defaultAction = defaultAction;
        this.label = label;
        this.modal = modal;
        this.icon = icon;
        this.formSubmitLabel = formSubmitLabel;
        this.pageName = pageName;
        this.upload = upload;
    }

    DefaultActions(boolean context, boolean defaultAction, boolean primary, String label, boolean modal,
                   String icon, String formSubmitLabel, String pageName, UploadType upload) {
        this.context = context;
        this.defaultAction = defaultAction;
        this.primary = primary;
        this.label = label;
        this.modal = modal;
        this.icon = icon;
        this.formSubmitLabel = formSubmitLabel;
        this.pageName = pageName;
        this.upload = upload;
    }

    DefaultActions(boolean context, boolean defaultAction, String label, boolean modal, String icon) {
        this.context = context;
        this.defaultAction = defaultAction;
        this.label = label;
        this.modal = modal;
        this.icon = icon;
    }

    public static DefaultActions get(String name) {
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

    public UploadType getUpload() {
        return upload;
    }
}