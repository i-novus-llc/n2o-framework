package net.n2oapp.framework.api.metadata.header;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.action.control.TargetEnum;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.badge.Badge;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
public class MenuItem extends Component implements Compiled, PropertiesAware, IdAware {
    @JsonProperty
    private String id;
    @JsonProperty
    private String title;
    @JsonProperty
    private String icon;
    @JsonProperty
    private PositionEnum iconPosition;
    @JsonProperty
    private String imageSrc;
    @JsonProperty
    private ShapeTypeEnum imageShape;
    @JsonProperty
    private String datasource;
    @JsonProperty("items")
    private ArrayList<MenuItem> subItems;
    @JsonProperty
    private String href;
    @JsonProperty
    private LinkTypeEnum linkType;
    @JsonProperty
    private TargetEnum target;
    @JsonProperty
    private Badge badge;
    @JsonProperty
    private Map<String, ModelLink> pathMapping;
    @JsonProperty
    private Map<String, ModelLink> queryMapping;
    @Deprecated
    private String pageId;
    private Map<String, Object> properties;
    @JsonProperty
    private Action action;

    @RequiredArgsConstructor
    @Getter
    public enum LinkTypeEnum implements N2oEnum {
        INNER("inner"),
        OUTER("outer");

        private final String id;
    }
}
