package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import net.n2oapp.framework.api.metadata.aware.IdAware;


public enum Position implements IdAware {
        left {
            @Override
            public String getId() {
                return "left";
            }
        },
        inline {
            @Override
            public String getId() {
                return "inline";
            }
        },
        right {
            @Override
            public String getId() {
                return "right";
            }
        }
    }