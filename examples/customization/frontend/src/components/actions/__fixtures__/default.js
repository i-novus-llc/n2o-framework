import React from "react";
import CustomAction from '../CustomAction';
import FactoryProvider from 'n2o/lib/core/factory/FactoryProvider';
import Factory from 'n2o/lib/core/factory/Factory';
import createFactoryConfig from 'n2o/lib/core/factory/createFactoryConfig';
const json = {
    "filterDefaultValues": {},
    "src": "FormWidget",
    actions: {
        "create": {
            "id": "create",
            "src": "CustomAction",
            "options": {
            }
        },
    },
    "toolbar": {
        "topLeft": [
            {
                "id": "topLeft0",
                "buttons": [
                    {
                        "id": "create",
                        "icon": "fa fa-plus",
                        "actionId": "create",
                        "hint": "Создать",
                        "hintPosition": "bottom",
                        "conditions": {},
                        "title": "Создать"
                    }
                ]
            }
        ],
    },
    "form": {
        "fieldsets": [
            {
                "src": "AddMemberFieldset",
                "image": "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/React-icon.svg/1200px-React-icon.svg.png",
                "repeat": "repeat",
                "backgroundImageSize": 40,
                "style": {
                    "padding": "40px"
                },
                "labalPosition": "right",
                "className": "tets",
                "rows": [
                    {
                        "cols": [
                            {
                                "fields": [
                                    {
                                        "id": "surname",
                                        "src": "StandardField",
                                        "label": "Фамилия",
                                        "dependency": [],
                                        "control": {
                                            "readOnly": false,
                                            "type": "text",
                                            "disabled": false,
                                            "src": "InputText"
                                        }
                                    }
                                ]
                            },
                            {
                                "fields": [
                                    {
                                        "id": "name",
                                        "src": "StandardField",
                                        "label": "Имя",
                                        "dependency": [],
                                        "control": {
                                            "readOnly": false,
                                            "type": "text",
                                            "disabled": false,
                                            "src": "InputText"
                                        }
                                    }
                                ]
                            }
                        ]
                    }
                ]
            }
        ],
        "validation": {},
        "fetchOnInit": false
    }
};
class CustomActionComponent extends React.Component {
    render() {
        return (
            <FactoryProvider config={createFactoryConfig({
                actions: {
                    CustomAction: CustomAction
                }
            })}>
                <Factory {...json}  />
            </FactoryProvider>
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
