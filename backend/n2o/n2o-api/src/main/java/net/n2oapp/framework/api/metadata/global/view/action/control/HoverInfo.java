package net.n2oapp.framework.api.metadata.global.view.action.control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*
 /* @author enuzhdina 
 /* @since 14.01.2016
 */
public class HoverInfo implements Serializable {
    private String src;
    private Element[] elements;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Element[] getElements() {
        return elements;
    }

    public void setElements(Element[] elements) {
        this.elements = elements;
    }

    public static class Element implements Serializable {
        private String label;
        private String fieldId;

        public Element() {
        }

        public Element(String label, String fieldId) {
            this.label = label;
            this.fieldId = fieldId;
        }

        public String getLabel() {
            return label;
        }

        public String getFieldId() {
            return fieldId;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setFieldId(String fieldId) {
            this.fieldId = fieldId;
        }
    }
}
