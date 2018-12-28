package net.n2oapp.framework.api.metadata.control;

import java.io.Serializable;

/**
* User: operhod
* Date: 19.08.14
* Time: 10:39
*/
public class ValidationReference implements Serializable {

    public enum Target implements Serializable {
        field, form
    }

    private String reference;
    private String side;
    private Target target;

    public ValidationReference(String reference, String side, Target target) {
        this.reference = reference;
        this.side = side;
        this.target = target;
    }

    public ValidationReference(String reference, String side) {
        this.reference = reference;
        this.side = side;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }
}
