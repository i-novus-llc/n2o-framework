package net.n2oapp.framework.sandbox.view;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.event.N2oEvent;

@Getter
@Setter
public class ProjectFileUpdateEvent extends N2oEvent {

    private String projectId;
    private String fileName;

    public ProjectFileUpdateEvent(Object source) {
        super(source);
    }

    public ProjectFileUpdateEvent(Object source, String projectId, String fileName) {
        super(source);
        this.source = source;
        this.fileName = fileName;
        this.projectId = projectId;
    }

}
