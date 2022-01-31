import React from 'react';
import { IntlProvider, addLocaleData } from 'react-intl';
import ruLocaleData from 'react-intl/locale-data/ru';
import createFactoryConfigLight from "n2o-framework/lib/core/factory/createFactoryConfigLight";
import FactoryProvider from "n2o-framework/lib/core/factory/FactoryProvider";
import Factory from "n2o-framework/lib/core/factory/Factory";
import JumbotronPage from "../JumbotronPage";
import { REGIONS } from "n2o-framework/src/core/factory/factoryLevels";

addLocaleData(ruLocaleData);

const Component = ({ regions, widgets, ...props }) => {
  return (
    <IntlProvider>
      <FactoryProvider config={createFactoryConfigLight({ pages: { JumbotronPage } })}>
        <div>
          <Factory src='JumbotronPage' {...props} />
          <div>
            {regions.map((place, key) => (
              <div className={`n2o-page__${key}`}>
                {place.panels.map((region, index) => (
                  <Factory
                    key={`region-${key}-${index}`}
                    level={REGIONS}
                    {...widgets[region.widgetId]}
                  />
                ))}
              </div>
            ))}
          </div>
        </div>
      </FactoryProvider>
    </IntlProvider>
  )
};

export default [
  {
    component: Component,
    name: "Кастомная страницы",
    props: {
      "regions": [
        {
          "src": "PanelRegion",
          "hasTabs": false,
          "headerTitle": "Данные пациента",
          "open": true,
          "collapsible": false,
          "fullScreen": false,
          "header": true,
          "panels": [
            {
              "id": "panel1",
              "label": "Пациенты",
              "opened": true,
              "widgetId": "proto_patients"
            }
          ]
        }
      ],
      "widgets": {
        "proto_patients": {
          "src": "AdvancedTableWidget",
          "dataProvider": {
            "url": "n2o/data/proto",
            "pathMapping": {},
            "queryMapping": {
              "patients_surname": {
                "value": "`surname`",
                "link": "models.filter['proto_patients']"
              },
              "patients_name": {
                "value": "`name`",
                "link": "models.filter['proto_patients']"
              },
              "patients_bdate_begin": {
                "value": "`bdate.begin`",
                "link": "models.filter['proto_patients']"
              },
              "patients_bdate_end": {
                "value": "`bdate.end`",
                "link": "models.filter['proto_patients']"
              },
              "patients_genders_id": {
                "value": "`genders.map(function(t){return t.id})`",
                "link": "models.filter['proto_patients']"
              },
              "patients_vip": {
                "value": "`vip`",
                "link": "models.filter['proto_patients']"
              }
            }
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
                  },
                  {
                    "id": "update_patient",
                    "icon": "fa fa-pencil",
                    "actionId": "update_patient",
                    "hint": "Изменить",
                    "hintPosition": "bottom",
                    "conditions": {
                      "enabled": [
                        {
                          "expression": "!_.isEmpty(this)",
                          "modelLink": "models.resolve['proto_patients']"
                        }
                      ]
                    },
                    "title": "Изменить"
                  },
                  {
                    "id": "delete",
                    "actionId": "delete",
                    "hint": "Удалить",
                    "hintPosition": "bottom",
                    "conditions": {
                      "enabled": [
                        {
                          "expression": "!_.isEmpty(this)",
                          "modelLink": "models.resolve['proto_patients']"
                        }
                      ]
                    },
                    "confirm": {
                      "text": "`'Вы уверены, что хотите удалить пациента ' + this.surname + ' ' + this.name + '?'`",
                      "title": "Предупреждение",
                      "okLabel": "Да",
                      "cancelLabel": "Нет",
                      "modelLink": "models.resolve['proto_patients']",
                      "mode": "modal"
                    },
                    "title": "Удалить"
                  }
                ]
              }
            ],
            "topRight": [
              {
                "id": "topRight0",
                "buttons": [
                  {
                    "id": "update2",
                    "icon": "fa fa-edit",
                    "actionId": "update2",
                    "hint": "Кнопка скрывается, если в строке имя Мария",
                    "hintPosition": "bottom",
                    "conditions": {
                      "enabled": [
                        {
                          "expression": "!_.isEmpty(this)",
                          "modelLink": "models.resolve['proto_patients']"
                        }
                      ],
                      "visible": [
                        {
                          "expression": "name!='Мария'",
                          "modelLink": "models.resolve['proto_patients']"
                        }
                      ]
                    },
                    "color": "light"
                  },
                  {
                    "id": "view",
                    "icon": "fa fa-book",
                    "actionId": "view",
                    "hint": "Кнопка блокируется, если в фильтре имя Анна",
                    "hintPosition": "right",
                    "conditions": {
                      "enabled": [
                        {
                          "expression": "name!='Анна'",
                          "modelLink": "models.resolve['proto_patients']"
                        },
                        {
                          "expression": "!_.isEmpty(this)",
                          "modelLink": "models.resolve['proto_patients']"
                        }
                      ]
                    },
                    "color": "info",
                    "title": "Просмотр"
                  }
                ]
              }
            ],
            "bottomRight": [
              {
                "id": "bottomRight0",
                "buttons": [
                  {
                    "id": "menuItem5",
                    "icon": "fa fa-filter",
                    "actionId": "menuItem5",
                    "hint": "Изменить видимость фильтров",
                    "hintPosition": "bottom",
                    "conditions": {}
                  },
                  {
                    "id": "menuItem6",
                    "icon": "fa fa-table",
                    "hint": "Изменить видимость колонок",
                    "hintPosition": "bottom",
                    "conditions": {},
                    "dropdownSrc": "ToggleColumn"
                  },
                  {
                    "id": "menuItem7",
                    "icon": "fa fa-refresh",
                    "actionId": "menuItem7",
                    "hint": "Обновить данные",
                    "hintPosition": "bottom",
                    "conditions": {}
                  },
                  {
                    "id": "menuItem8",
                    "icon": "fa fa-bars",
                    "hint": "Изменить размер",
                    "hintPosition": "bottom",
                    "conditions": {},
                    "dropdownSrc": "ChangeSize"
                  }
                ]
              }
            ]
          },
          "actions": {
            "update_birthday": {
              "id": "update_birthday",
              "src": "perform",
              "options": {
                "type": "n2o/actionImpl/START_INVOKE",
                "payload": {
                  "widgetId": "proto_patients",
                  "dataProvider": {
                    "url": "n2o/data/proto/:proto_patients_id/update_birthday",
                    "pathMapping": {
                      "proto_patients_id": {
                        "link": "models.resolve['proto_patients'].id"
                      }
                    },
                    "method": "POST"
                  },
                  "modelLink": "models.resolve['proto_patients']"
                },
                "meta": {
                  "success": {
                    "refresh": {
                      "type": "widget",
                      "options": {
                        "widgetId": "proto_patients"
                      }
                    }
                  },
                  "fail": {}
                }
              }
            },
            "create": {
              "id": "create",
              "src": "perform",
              "options": {
                "type": "n2o/modals/INSERT",
                "payload": {
                  "name": "proto_create",
                  "pageId": "proto_create",
                  "pageUrl": "/proto/create",
                  "pathMapping": {},
                  "queryMapping": {},
                  "closeButton": true,
                  "visible": true
                }
              }
            },
            "update_patient": {
              "id": "update_patient",
              "src": "perform",
              "options": {
                "type": "n2o/modals/INSERT",
                "payload": {
                  "name": "proto_update_patient",
                  "pageId": "proto_update_patient",
                  "pageUrl": "/proto/:proto_patients_id/update_patient",
                  "pathMapping": {
                    "proto_patients_id": {
                      "link": "models.resolve['proto_patients'].id"
                    }
                  },
                  "queryMapping": {},
                  "closeButton": true,
                  "visible": true
                }
              }
            },
            "delete": {
              "id": "delete",
              "src": "perform",
              "options": {
                "type": "n2o/actionImpl/START_INVOKE",
                "payload": {
                  "widgetId": "proto_patients",
                  "dataProvider": {
                    "url": "n2o/data/proto/:proto_patients_id/delete",
                    "pathMapping": {
                      "proto_patients_id": {
                        "link": "models.resolve['proto_patients'].id"
                      }
                    },
                    "method": "POST"
                  },
                  "modelLink": "models.resolve['proto_patients']"
                },
                "meta": {
                  "success": {
                    "refresh": {
                      "type": "widget",
                      "options": {
                        "widgetId": "proto_patients"
                      }
                    }
                  },
                  "fail": {}
                }
              }
            },
            "update2": {
              "id": "update2",
              "src": "link",
              "options": {
                "path": "/proto/:proto_patients_id/update2",
                "target": "application",
                "pathMapping": {
                  "proto_patients_id": {
                    "link": "models.resolve['proto_patients'].id"
                  }
                },
                "queryMapping": {}
              }
            },
            "view": {
              "id": "view",
              "src": "perform",
              "options": {
                "type": "n2o/modals/INSERT",
                "payload": {
                  "name": "proto_view",
                  "pageId": "proto_view",
                  "pageUrl": "/proto/:proto_patients_id/view",
                  "pathMapping": {
                    "proto_patients_id": {
                      "link": "models.resolve['proto_patients'].id"
                    }
                  },
                  "queryMapping": {},
                  "closeButton": true,
                  "visible": true
                }
              }
            },
            "menuItem5": {
              "id": "menuItem5",
              "src": "perform",
              "options": {
                "payload": {
                  "widgetId": "proto_patients"
                },
                "type": "n2o/widgets/TOGGLE_FILTER_VISIBILITY"
              }
            },
            "menuItem7": {
              "id": "menuItem7",
              "src": "perform",
              "options": {
                "type": "n2o/widgets/DATA_REQUEST",
                "payload": {
                  "widgetId": "proto_patients"
                }
              }
            },
            "surname": {
              "id": "surname",
              "src": "link",
              "options": {
                "path": "/proto/:proto_patients_id/surname",
                "target": "application",
                "pathMapping": {
                  "proto_patients_id": {
                    "link": "models.resolve['proto_patients'].id"
                  }
                },
                "queryMapping": {}
              }
            },
            "name": {
              "id": "name",
              "src": "perform",
              "options": {
                "type": "n2o/modals/INSERT",
                "payload": {
                  "name": "proto_name",
                  "pageId": "proto_name",
                  "pageUrl": "/proto/:proto_patients_id/name",
                  "pathMapping": {
                    "proto_patients_id": {
                      "link": "models.resolve['proto_patients'].id"
                    }
                  },
                  "queryMapping": {},
                  "closeButton": true,
                  "visible": true
                }
              }
            },
            "vip": {
              "id": "vip",
              "src": "perform",
              "options": {
                "type": "n2o/actionImpl/START_INVOKE",
                "payload": {
                  "widgetId": "proto_patients",
                  "dataProvider": {
                    "url": "n2o/data/proto/:proto_patients_id/vip",
                    "pathMapping": {
                      "proto_patients_id": {
                        "link": "models.resolve['proto_patients'].id"
                      }
                    },
                    "method": "POST"
                  },
                  "modelLink": "models.resolve['proto_patients']"
                },
                "meta": {
                  "success": {
                    "refresh": {
                      "type": "widget",
                      "options": {
                        "widgetId": "proto_patients"
                      }
                    }
                  },
                  "fail": {}
                }
              }
            },
            "update": {
              "id": "update",
              "src": "perform",
              "options": {
                "type": "n2o/modals/INSERT",
                "payload": {
                  "name": "proto_update",
                  "pageId": "proto_update",
                  "pageUrl": "/proto/:proto_patients_id/update",
                  "pathMapping": {
                    "proto_patients_id": {
                      "link": "models.resolve['proto_patients'].id"
                    }
                  },
                  "queryMapping": {},
                  "closeButton": true,
                  "visible": true
                }
              }
            }
          },
          "paging": {
            "size": 10
          },
          "filter": {
            "filterFieldsets": [
              {
                "src": "StandardFieldset",
                "rows": [
                  {
                    "cols": [
                      {
                        "fields": [
                          {
                            "src": "StandardField",
                            "id": "surname",
                            "label": "Фамилия",
                            "dependency": [],
                            "control": {
                              "src": "InputText",
                              "id": "surname"
                            }
                          }
                        ]
                      },
                      {
                        "fields": [
                          {
                            "src": "StandardField",
                            "id": "name",
                            "label": "Имя",
                            "dependency": [],
                            "control": {
                              "src": "InputText",
                              "id": "name"
                            }
                          }
                        ]
                      },
                      {
                        "fields": [
                          {
                            "src": "StandardField",
                            "id": "bdate",
                            "label": "Дата рождения",
                            "dependency": [],
                            "control": {
                              "src": "DateInterval",
                              "id": "bdate",
                              "dateFormat": "DD.MM.YYYY",
                              "utc": false,
                              "outputFormat": "YYYY-MM-DDTHH:mm:ss"
                            }
                          }
                        ]
                      }
                    ]
                  },
                  {
                    "cols": [
                      {
                        "size": 10,
                        "fields": [
                          {
                            "src": "StandardField",
                            "id": "genders",
                            "label": "Пол",
                            "dependency": [],
                            "control": {
                              "src": "CheckboxGroup",
                              "id": "genders",
                              "valueFieldId": "id",
                              "labelFieldId": "name",
                              "dataProvider": {
                                "url": "n2o/data/ProtoGender",
                                "queryMapping": {}
                              },
                              "inline": true,
                              "type": "n2o"
                            }
                          }
                        ]
                      },
                      {
                        "fields": [
                          {
                            "src": "StandardField",
                            "id": "vip",
                            "dependency": [],
                            "control": {
                              "src": "Checkbox",
                              "id": "vip",
                              "label": "VIP",
                              "defaultUnchecked": "null"
                            }
                          }
                        ]
                      }
                    ]
                  }
                ]
              }
            ],
            "filterButtonId": "filter",
            "blackResetList": [],
            "filterPlace": "top",
            "validation": {}
          },
          "children": "collapse",
          "table": {
            "autoFocus": true,
            "size": 10,
            "hasFocus": true,
            "hasSelect": true,
            "cells": [
              {
                "id": "surname",
                "fieldKey": "surname",
                "src": "LinkCell",
                "actionId": "surname",
                "action": {
                  "id": "surname",
                  "src": "link",
                  "options": {
                    "path": "/proto/:proto_patients_id/surname",
                    "target": "application",
                    "pathMapping": {
                      "proto_patients_id": {
                        "link": "models.resolve['proto_patients'].id"
                      }
                    },
                    "queryMapping": {}
                  }
                }
              },
              {
                "id": "name",
                "fieldKey": "name",
                "src": "LinkCell",
                "actionId": "name",
                "action": {
                  "id": "name",
                  "src": "perform",
                  "options": {
                    "type": "n2o/modals/INSERT",
                    "payload": {
                      "name": "proto_name",
                      "pageId": "proto_name",
                      "pageUrl": "/proto/:proto_patients_id/name",
                      "pathMapping": {
                        "proto_patients_id": {
                          "link": "models.resolve['proto_patients'].id"
                        }
                      },
                      "queryMapping": {},
                      "closeButton": true,
                      "visible": true
                    }
                  }
                }
              },
              {
                "id": "patrName",
                "fieldKey": "patrName",
                "src": "LinkCell",
                "url": "`'/'+id+'/update2'`",
                "target": "application",
                "visible": "`name!='Мария'`"
              },
              {
                "id": "birthday",
                "fieldKey": "birthday",
                "src": "EditableCell",
                "actionId": "update_birthday",
                "control": {
                  "src": "DatePicker",
                  "id": "birthday",
                  "dateFormat": "DD.MM.YYYY",
                  "utc": false,
                  "outputFormat": "YYYY-MM-DDTHH:mm:ss"
                },
                "format": "date DD.MM.YYYY",
                "editFieldId": "birthday",
                "editType": "inline",
                "action": {
                  "id": "update_birthday",
                  "src": "perform",
                  "options": {
                    "type": "n2o/actionImpl/START_INVOKE",
                    "payload": {
                      "widgetId": "proto_patients",
                      "dataProvider": {
                        "url": "n2o/data/proto/:proto_patients_id/update_birthday",
                        "pathMapping": {
                          "proto_patients_id": {
                            "link": "models.resolve['proto_patients'].id"
                          }
                        },
                        "method": "POST"
                      },
                      "modelLink": "models.resolve['proto_patients']"
                    },
                    "meta": {
                      "success": {
                        "refresh": {
                          "type": "widget",
                          "options": {
                            "widgetId": "proto_patients"
                          }
                        }
                      },
                      "fail": {}
                    }
                  }
                },
                "editable": true
              },
              {
                "id": "genderName",
                "fieldKey": "genderName",
                "src": "BadgeCell",
                "color": "`gender.id==1?'danger':'success'`"
              },
              {
                "id": "vip",
                "fieldKey": "vip",
                "src": "CheckboxCell",
                "actionId": "vip",
                "disabled": "`!(name!='Мария')`",
                "action": {
                  "id": "vip",
                  "src": "perform",
                  "options": {
                    "type": "n2o/actionImpl/START_INVOKE",
                    "payload": {
                      "widgetId": "proto_patients",
                      "dataProvider": {
                        "url": "n2o/data/proto/:proto_patients_id/vip",
                        "pathMapping": {
                          "proto_patients_id": {
                            "link": "models.resolve['proto_patients'].id"
                          }
                        },
                        "method": "POST"
                      },
                      "modelLink": "models.resolve['proto_patients']"
                    },
                    "meta": {
                      "success": {
                        "refresh": {
                          "type": "widget",
                          "options": {
                            "widgetId": "proto_patients"
                          }
                        }
                      },
                      "fail": {}
                    }
                  }
                }
              },
              {
                "id": "",
                "fieldKey": "",
                "src": "ButtonsCell",
                "toolbar": [{
                  "buttons": [
                    {
                      "id": "subMenu0",
                      "icon": "fa fa-ellipsis-v",
                      "conditions": {},
                      "color": "link",
                      "subMenu": [
                        {
                          "id": "delete",
                          "icon": "fa fa-trash",
                          "actionId": "delete",
                          "confirm": {
                            "text": "`'Вы уверены, что хотите удалить пациента ' + this.surname + ' ' + this.name + '?'`",
                            "title": "Предупреждение",
                            "okLabel": "Да",
                            "cancelLabel": "Нет",
                            "modelLink": "models.resolve['proto_patients']",
                            "mode": "modal"
                          },
                          "title": "Удалить"
                        },
                        {
                          "id": "update",
                          "icon": "fa fa-pencil",
                          "actionId": "update",
                          "title": "Изменить"
                        }
                      ]
                    }
                  ]
                }],
                "actions": {
                  "delete": {
                    "id": "delete",
                    "src": "perform",
                    "options": {
                      "type": "n2o/actionImpl/START_INVOKE",
                      "payload": {
                        "widgetId": "proto_patients",
                        "dataProvider": {
                          "url": "n2o/data/proto/:proto_patients_id/delete",
                          "pathMapping": {
                            "proto_patients_id": {
                              "link": "models.resolve['proto_patients'].id"
                            }
                          },
                          "method": "POST"
                        },
                        "modelLink": "models.resolve['proto_patients']"
                      },
                      "meta": {
                        "success": {
                          "refresh": {
                            "type": "widget",
                            "options": {
                              "widgetId": "proto_patients"
                            }
                          }
                        },
                        "fail": {}
                      }
                    }
                  },
                  "update": {
                    "id": "update",
                    "src": "perform",
                    "options": {
                      "type": "n2o/modals/INSERT",
                      "payload": {
                        "name": "proto_update",
                        "pageId": "proto_update",
                        "pageUrl": "/proto/:proto_patients_id/update",
                        "pathMapping": {
                          "proto_patients_id": {
                            "link": "models.resolve['proto_patients'].id"
                          }
                        },
                        "queryMapping": {},
                        "closeButton": true,
                        "visible": true
                      }
                    }
                  }
                }
              }
            ],
            "headers": [
              {
                "id": "surname",
                "label": "Фамилия",
                "src": "TextTableHeader",
                "sortable": true
              },
              {
                "id": "name",
                "label": "Имя",
                "src": "TextTableHeader",
                "sortable": true
              },
              {
                "id": "patrName",
                "label": "Отчество",
                "src": "TextTableHeader",
                "sortable": true
              },
              {
                "id": "birthday",
                "label": "Дата рождения",
                "src": "TextTableHeader",
                "sortable": true
              },
              {
                "id": "genderName",
                "label": "Пол",
                "src": "TextTableHeader",
                "sortable": false
              },
              {
                "id": "vip",
                "label": "VIP",
                "src": "TextTableHeader",
                "sortable": false
              },
              {
                "id": "",
                "src": "TextTableHeader"
              }
            ],
            "sorting": {
              "birthday": "ASC"
            }
          }
        }
      },
    },
    reduxState: {}
  }
];
