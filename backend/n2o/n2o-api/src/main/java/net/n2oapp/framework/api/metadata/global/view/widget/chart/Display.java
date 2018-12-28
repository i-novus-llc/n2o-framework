package net.n2oapp.framework.api.metadata.global.view.widget.chart;

import net.n2oapp.framework.api.metadata.global.aware.IdAware;

/**
 * @author V. Alexeev.
 * @date 02.03.2016
 */
public enum Display implements IdAware{

    LINE {
        @Override
        public String getId() {
            return "line";
        }

        @Override
        public Type getType() {
            return Type.LINE;
        }
    },
    BAR {
        @Override
        public String getId() {
            return "bar";
        }

        @Override
        public Type getType() {
            return Type.LINE;
        }
    },
    RADAR {
        @Override
        public String getId() {
            return "radar";
        }

        @Override
        public Type getType() {
            return Type.LINE;
        }
    },
    POLAR {
        @Override
        public String getId() {
            return "polar";
        }

        @Override
        public Type getType() {
            return Type.CIRCLE;
        }
    },
    PIE {
        @Override
        public String getId() {
            return "pie";
        }

        @Override
        public Type getType() {
            return Type.CIRCLE;
        }
    },
    DOUGHNUT {
        @Override
        public String getId() {
            return "doughnut";
        }

        @Override
        public Type getType() {
            return Type.CIRCLE;
        }
    };

    public abstract Type getType();

    public enum Type {
        LINE,
        CIRCLE
    }

}
