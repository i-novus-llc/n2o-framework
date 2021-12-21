package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * Клиентская модель поля
 */
@Getter
@Setter
public class Field extends Component {
    @JsonProperty
    private String id;
    @JsonProperty
    private Boolean required;
    @JsonProperty
    private Boolean visible;
    @JsonProperty
    private Boolean enabled;
    @JsonProperty
    private String label;
    @JsonProperty
    private Boolean noLabelBlock;
    @JsonProperty
    private String labelClass;
    @JsonProperty
    private String description;
    @JsonProperty
    private String help;
    @JsonProperty
    private Group[] toolbar;
    @JsonProperty("dependency")
    private List<ControlDependency> dependencies = new ArrayList<>();

    public void addDependency(ControlDependency dependency) {
        dependencies.add(dependency);
    }
}
