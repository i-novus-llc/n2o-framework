import React from "react";
import Factory from "n2o-framework/lib/core/factory/Factory";
import AddMemberFieldset from "../AddMemberFieldset";
import FactoryProvider from "n2o-framework/lib/core/factory/FactoryProvider";
import createConfig from "n2o-framework/lib/core/factory/createFactoryConfig";
const defaultMeta = {
  id: "proto",
  routes: {
    list: [
      {
        path: "/proto",
        exact: true,
        isOtherPage: false
      }
    ]
  },
  widgets: {
    image_control_widget: {
      id: "test",
      filterDefaultValues: {},
      src: "FormWidget",
      form: {
        fieldsets: [
          {
            src: "AddMemberFieldset",
            image:
              "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a7/React-icon.svg/1200px-React-icon.svg.png",
            repeat: "repeat",
            backgroundImageSize: 40,
            style: {
              padding: "40px"
            },
            labalPosition: "right",
            className: "tets",
            rows: [
              {
                cols: [
                  {
                    fields: [
                      {
                        id: "surname",
                        src: "StandardField",
                        label: "Фамилия55",
                        dependency: [],
                        control: {
                          readOnly: false,
                          type: "text",
                          disabled: false,
                          src: "InputText"
                        }
                      }
                    ]
                  },
                  {
                    fields: [
                      {
                        id: "name",
                        src: "StandardField",
                        label: "Имя",
                        dependency: [],
                        control: {
                          readOnly: false,
                          type: "text",
                          disabled: false,
                          src: "InputText"
                        }
                      }
                    ]
                  }
                ]
              }
            ]
          }
        ],
        validation: {},
      }
    }
  },
  breadcrumb: [
    {
      label: "Fieldsets/AddMemberFieldset"
    }
  ],
  actions: {},
  layout: {
    src: "SingleLayout",
    regions: {
      single: [
        {
          src: "NoneRegion",
          items: [
            {
              widgetId: "image_control_widget"
            }
          ]
        }
      ]
    }
  },
  page: {
    title: "Пример AddMemberFieldset"
  }
};

const AddMemberExample = props => (
  <FactoryProvider
    config={createConfig({
      fieldsets: {
        AddMemberFieldset: AddMemberFieldset
      }
    })}
    {...defaultMeta}
  >
    <Factory sr {...defaultMeta["widgets"]["image_control_widget"]} />
  </FactoryProvider>
);

export default [
  {
    component: AddMemberExample,
    name: "AddMemberFieldset",
    props: defaultMeta,
    reduxState: {}
  }
];
