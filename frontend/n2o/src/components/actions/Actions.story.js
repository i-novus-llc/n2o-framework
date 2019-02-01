import React from 'react';
import { storiesOf } from '@storybook/react';

import Actions from './Actions';
import MetaJson from './WidgetActions.meta.json';
import AuthButtonContainer from '../../core/auth/AuthLogin';

const stories = storiesOf('Действия', module);

stories
  .add('Варианты кнопок', () => {
    const toolbar1 = [
      {
        buttons: [
          {
            id: 'testBtn21',
            title: 'Кнопка',
            actionId: 'dummy'
          }
        ]
      }
    ];
    const toolbar2 = [
      {
        buttons: [
          {
            id: 'testBtn22',
            title: 'Кнопка',
            actionId: 'dummy'
          },
          {
            id: 'testBtn23',
            title: 'Еще кнопка',
            actionId: 'dummy'
          }
        ]
      }
    ];
    const toolbar3 = [
      {
        buttons: [
          {
            id: 'testBtn24',
            title: 'Кнопка',
            actionId: 'dummy'
          },
          {
            id: 'testBtn25',
            title: 'Еще кнопка',
            actionId: 'dummy'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn26',
            title: 'Кнопка',
            actionId: 'dummy'
          },
          {
            id: 'testBtn27',
            title: 'Еще кнопка',
            actionId: 'dummy'
          }
        ]
      }
    ];
    const toolbar4 = [
      {
        buttons: [
          {
            id: 'testBtn28',
            title: 'Дропдаун',
            subMenu: [
              {
                id: 'testBtn29',
                actionId: 'dummy',
                title: 'Элемент списка'
              },
              {
                id: 'testBtn29',
                actionId: 'dummy',
                title: 'Элемент списка'
              }
            ]
          }
        ]
      }
    ];

    const toolbar5 = [
      {
        buttons: [
          {
            id: 'testBtn30',
            title: 'Кнопка',
            actionId: 'dummy'
          },
          {
            id: 'testBtn31',
            title: 'Дропдаун',
            subMenu: [
              {
                id: 'testBtn32',
                actionId: 'dummy',
                title: 'Элемент списка'
              }
            ]
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn33',
            title: 'Дропдаун',
            subMenu: [
              {
                id: 'testBtn34',
                actionId: 'dummy',
                title: 'Элемент списка'
              }
            ]
          },
          {
            id: 'testBtn35',
            title: 'Кнопка',
            actionId: 'dummy'
          }
        ]
      }
    ];

    const colorToolbar = [
      {
        buttons: [
          {
            id: 'testBtn1',
            title: 'Кнопка',
            actionId: 'dummy',
            color: 'default'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn2',
            title: 'Кнопка',
            actionId: 'dummy',
            color: 'primary'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn3',
            title: 'Кнопка',
            actionId: 'dummy',
            color: 'success'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn4',
            title: 'Кнопка',
            actionId: 'dummy',
            color: 'info'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn5',
            title: 'Кнопка',
            actionId: 'dummy',
            color: 'warning'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn6',
            title: 'Кнопка',
            actionId: 'dummy',
            color: 'danger'
          }
        ]
      }
    ];

    const iconToolbar = [
      {
        buttons: [
          {
            id: 'testBtn7',
            title: 'Apple',
            actionId: 'dummy',
            icon: 'fa fa-apple'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn8',
            title: 'Github',
            actionId: 'dummy',
            icon: 'fa fa-github'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn9',
            title: 'Telegram',
            actionId: 'dummy',
            icon: 'fa fa-telegram'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn10',
            title: 'Vk',
            actionId: 'dummy',
            icon: 'fa fa-vk'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn11',
            title: 'Imdb',
            actionId: 'dummy',
            icon: 'fa fa-imdb'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn12',
            title: 'Facebook',
            actionId: 'dummy',
            icon: 'fa fa-facebook'
          }
        ]
      }
    ];

    const iconOnlyToolbar = [
      {
        buttons: [
          {
            id: 'testBtn13',
            actionId: 'dummy',
            icon: 'fa fa-apple'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn14',
            actionId: 'dummy',
            icon: 'fa fa-github'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn15',
            actionId: 'dummy',
            icon: 'fa fa-telegram'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn16',
            actionId: 'dummy',
            icon: 'fa fa-vk'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn17',
            actionId: 'dummy',
            icon: 'fa fa-imdb'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn18',
            actionId: 'dummy',
            icon: 'fa fa-facebook'
          }
        ]
      }
    ];

    const sizeToolbarLg = [
      {
        buttons: [
          {
            id: 'small',
            actionId: 'dummy',
            title: 'Большая',
            size: 'lg'
          }
        ]
      }
    ];

    const sizeToolbar = [
      {
        buttons: [
          {
            id: 'small',
            actionId: 'dummy',
            title: 'Стандартная'
          }
        ]
      }
    ];

    const sizeToolbarSm = [
      {
        buttons: [
          {
            id: 'small',
            actionId: 'dummy',
            title: 'Маленькая',
            size: 'sm'
          }
        ]
      }
    ];

    const actions = {
      dummy: {
        src: 'dummyImpl'
      }
    };

    return (
      <React.Fragment>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={toolbar1} containerKey="test" />
        </div>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={toolbar2} containerKey="test" />
        </div>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={toolbar3} containerKey="test" />
        </div>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={toolbar4} containerKey="test" />
        </div>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={toolbar5} containerKey="test" />
        </div>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={colorToolbar} containerKey="test" />
        </div>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={iconToolbar} containerKey="test" />
        </div>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={iconOnlyToolbar} containerKey="test" />
        </div>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={sizeToolbarLg} containerKey="large" />
        </div>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={sizeToolbar} containerKey="default" />
        </div>
        <div className="row mb-2">
          <Actions actions={actions} toolbar={sizeToolbarSm} containerKey="sm" />
        </div>
      </React.Fragment>
    );
  })
  .add('Метаданные для виджета', () => (
    <Actions
      actions={MetaJson.actions}
      toolbar={MetaJson.toolbar.topLeft}
      containerKey="metaBtns"
    />
  ))
  .add('Ограничение доступа на кнопки', () => {
    const toolbar = [
      {
        buttons: [
          {
            id: 'testBtn22',
            title: 'Кнопка',
            actionId: 'dummy'
          },
          {
            id: 'testBtn23',
            title: 'Защищенная кнопка',
            actionId: 'dummy',
            security: {
              roles: ['admin']
            }
          }
        ]
      }
    ];
    return (
      <div>
        <small>
          Введите <mark>admin</mark>, чтобы увидеть скрытую кнопку
        </small>
        <AuthButtonContainer />
        <br />
        <Actions actions={MetaJson.actions} toolbar={toolbar} containerKey="metaBtns" />
      </div>
    );
  })
  .add('Ограничение доступа на выпадающие списки', () => {
    const toolbar = [
      {
        buttons: [
          {
            id: 'testBtn22',
            title: 'Кнопка',
            actionId: 'dummy',
            subMenu: [
              {
                id: 'testBtn23',
                title: 'Защищенная кнопка',
                actionId: 'dummy',
                security: {
                  roles: ['admin']
                }
              },
              {
                id: 'testBtn23',
                title: 'Защищенная кнопка 2',
                actionId: 'dummy',
                security: {
                  roles: ['admin']
                }
              }
            ]
          }
        ]
      }
    ];
    return (
      <div>
        <small>
          Введите <mark>admin</mark>, чтобы увидеть выпадающий список
        </small>
        <AuthButtonContainer />
        <br />
        <Actions actions={MetaJson.actions} toolbar={toolbar} containerKey="metaBtns" />
      </div>
    );
  })
  .add('Page Buttons с предустановленным visible/disabled', () => {
    const actions = {
      dummy: {
        src: 'dummyImpl'
      }
    };
    const toolbarNonVisible = [
      {
        buttons: [
          {
            id: '111',
            title: 'Кнопка',
            actionId: 'dummy',
            visible: false
          }
        ]
      }
    ];
    const toolbarDisabled = [
      {
        buttons: [
          {
            id: '1112',
            title: 'Кнопка',
            actionId: 'dummy',
            disabled: true
          }
        ]
      }
    ];
    const dropDownNonVisisble = [
      {
        buttons: [
          {
            id: '11113',
            title: 'Дропдаун',
            visible: false,
            subMenu: [
              {
                id: 'testBtn29',
                actionId: 'dummy',
                title: 'Элемент списка'
              },
              {
                id: 'testBtn29',
                actionId: 'dummy',
                title: 'Элемент списка'
              }
            ]
          }
        ]
      }
    ];
    const dropDownDisabled = [
      {
        buttons: [
          {
            id: '11134',
            title: 'Дропдаун',
            disabled: true,
            subMenu: [
              {
                id: 'testBtn29',
                actionId: 'dummy',
                title: 'Элемент списка'
              },
              {
                id: 'testBtn29',
                actionId: 'dummy',
                title: 'Элемент списка'
              }
            ]
          }
        ]
      }
    ];

    return (
      <React.Fragment>
        <div className="row mb-2">
          Кнопку не видно, потому что она скрыта
          <Actions actions={actions} toolbar={toolbarNonVisible} containerKey="visibleTest" />
        </div>
        <div className="row mb-2">
          Кнопка неактивна
          <Actions actions={actions} toolbar={toolbarDisabled} containerKey="disabledTest" />
        </div>
        <div className="row mb-2">
          Дропдаун не видно, потому что он скрыт
          <Actions
            actions={actions}
            toolbar={dropDownNonVisisble}
            containerKey="notVisibleDropDown"
          />
        </div>
        <div className="row mb-2">
          Дропдаун неактивен
          <Actions actions={actions} toolbar={dropDownDisabled} containerKey="dropdownDisabled" />
        </div>
      </React.Fragment>
    );
  });
