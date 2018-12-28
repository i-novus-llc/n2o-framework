package net.n2oapp.framework.api.ui;

import net.n2oapp.framework.api.metadata.event.action.UploadType;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;

/**
 * Created by schirkova on 29.09.2014.
 */
@Deprecated //see UploadType
public enum FormModel implements IdAware {
    QUERY("query", UploadType.query),
    DEFAULT("default", UploadType.defaults),
    COPY("copy", UploadType.copy),
    QUERY_OR_DEFAULT("query-or-default", null),
    CONTROL("control", UploadType.resolve),
    BULK("bulk", null);

    private String id;
    private UploadType upload;

    FormModel(String id, UploadType upload) {
        this.id = id;
        this.upload = upload;
    }

    @Override
    public String getId() {
        return id;
    }

    public UploadType getUpload() {
        return upload;
    }

    public static FormModel fromUploadType(UploadType uploadType) {
        for (FormModel model : values()) {
            if (model.getUpload().equals(uploadType)) {
                return model;
            }
        }
        return null;
    }

    public static FormModel getByName(String name) {
        for (FormModel model : values()) {
            if (model.getId().equalsIgnoreCase(name)) {
                return model;
            }
        }
        return null;
    }
}
