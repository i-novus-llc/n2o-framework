package net.n2oapp.framework.api.metadata.global.view.widget.list;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oAbstractListWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.N2oSimpleColumn;

@Getter
@Setter
public class N2oListWidget extends N2oAbstractListWidget {
    private ContentElement[] content;

    public static abstract class ContentElement extends N2oSimpleColumn {
        public abstract String getPlace();
    }

    public static class LeftTop extends ContentElement {
        @Override
        public String getPlace() {
            return "leftTop";
        }
    }

    public static class LeftBottom extends ContentElement {
        @Override
        public String getPlace() {
            return "leftBottom";
        }
    }

    public static class Header extends ContentElement {
        @Override
        public String getPlace() {
            return "header";
        }
    }

    public static class Body extends ContentElement {
        @Override
        public String getPlace() {
            return "body";
        }
    }

    public static class SubHeader extends ContentElement {
        @Override
        public String getPlace() {
            return "subHeader";
        }
    }

    public static class RightTop extends ContentElement {
        @Override
        public String getPlace() {
            return "rightTop";
        }
    }

    public static class RightBottom extends ContentElement {
        @Override
        public String getPlace() {
            return "rightBottom";
        }
    }

    public static class Extra extends ContentElement {
        @Override
        public String getPlace() {
            return "extra";
        }
    }
}
