import React from "react";
import FactoryProvider from 'n2o/lib/core/factory/FactoryProvider';
import Factory from 'n2o/lib/core/factory/Factory';
import Actions from 'n2o/lib/components/actions/Actions';
import createFactoryConfig from 'n2o/lib/core/factory/createFactoryConfig';

const actions = {
    "test": {
        "src": "dummyImpl",
            "options": {
            "testProp": "testValueProp"
        }
    }
};

const toolbar = [
    {
        "id": "topLeft0",
        "buttons": [
            {
                "id": "test",
                "icon": "fa fa-plus",
                "actionId": "test",
                "hint": "Пример вызова",
                "hintPosition": "bottom",
                "conditions": {},
                "title": "Пример вызова "
            }
        ]
    }
];

class CustomActionComponent extends React.Component {
    render() {
        return (
            <Actions actions={actions} toolbar={toolbar}  />
        );
    }
}

CustomActionComponent.propTypes = {};

export default [
    {
        component: CustomActionComponent,
        name: "Кастомное действие",
        props: {

        },
        reduxState: {}
    }
];
