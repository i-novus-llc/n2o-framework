package net.n2oapp.framework.api.metadata.control;

import net.n2oapp.framework.api.metadata.global.aware.IdAware;

import java.io.Serializable;
import java.util.List;

/**
 * @author V. Alexeev.
 * @date 09.03.2016
 */
public class Validations implements Serializable{

    private List<ValidationReference> validationReferences;
    private Display display;
    private Position position;

    public List<ValidationReference> getValidationReferences() {
        return validationReferences;
    }

    public void setValidationReferences(List<ValidationReference> validationReferences) {
        this.validationReferences = validationReferences;
    }

    public Display getDisplay() {
        return display;
    }

    public void setDisplay(Display display) {
        this.display = display;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public enum Position implements IdAware {
        RIGHT {
            @Override
            public String getId() {
                return "right";
            }
        },
        BOTTOM {
            @Override
            public String getId() {
                return "bottom";
            }
        },
        TOP {
            @Override
            public String getId() {
                return "top";
            }
        };
    }

    public enum Display implements IdAware{

        BUBBLE {
            @Override
            public String getId() {
                return "bubble";
            }
        },
        TEXT {
            @Override
            public String getId() {
                return "text";
            }
        };

    }

}
