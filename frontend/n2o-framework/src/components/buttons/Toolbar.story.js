import React from 'react';
import { storiesOf } from '@storybook/react';

import Toolbar from './Toolbar';
import MetaJson from './Toolbar.meta.json';
import AuthButtonContainer from '../../core/auth/AuthLogin';

const stories = storiesOf('Кнопки/Тулбар', module);

stories
  .add('Варианты кнопок', () => {
    const toolbar1 = [
      {
        buttons: [
          {
            id: 'testBtn21',
            label: 'Кнопка',
            src: 'PerformButton'
          }
        ]
      }
    ];
    const toolbar2 = [
      {
        buttons: [
          {
            id: 'testBtn22',
            label: 'Кнопка',
            src: 'PerformButton'
          },
          {
            id: 'testBtn23',
            label: 'Еще кнопка',
            src: 'PerformButton'
          }
        ]
      }
    ];
    const toolbar3 = [
      {
        buttons: [
          {
            id: 'testBtn24',
            label: 'Кнопка',
            src: 'PerformButton'
          },
          {
            id: 'testBtn25',
            label: 'Еще кнопка',
            src: 'PerformButton'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn26',
            label: 'Кнопка',
            src: 'PerformButton'
          },
          {
            id: 'testBtn27',
            label: 'Еще кнопка',
            src: 'PerformButton'
          }
        ]
      }
    ];
    const toolbar4 = [
      {
        buttons: [
          {
            id: 'testBtn28',
            label: 'Дропдаун',
            src: 'DropdownButton',
            subMenu: [
              {
                id: 'testBtn29',
                actionId: 'dummy',
                label: 'Элемент списка'
              },
              {
                id: 'testBtn29',
                actionId: 'dummy',
                label: 'Элемент списка'
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
            label: 'Кнопка',
            src: 'PerformButton',
            actionId: 'dummy'
          },
          {
            id: 'testBtn31',
            label: 'Дропдаун',
            src: 'DropdownButton',
            subMenu: [
              {
                id: 'testBtn32',
                label: 'Элемент списка'
              }
            ]
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn33',
            label: 'Дропдаун',
            src: 'DropdownButton',
            subMenu: [
              {
                id: 'testBtn34',
                actionId: 'dummy',
                label: 'Элемент списка'
              }
            ]
          },
          {
            id: 'testBtn35',
            label: 'Кнопка',
            src: 'PerformButton'
          }
        ]
      }
    ];

    const colorToolbar = [
      {
        buttons: [
          {
            id: 'testBtn1',
            label: 'Кнопка',
            src: 'PerformButton',
            color: 'default'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn2',
            label: 'Кнопка',
            src: 'PerformButton',
            color: 'primary'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn3',
            label: 'Кнопка',
            src: 'PerformButton',
            color: 'success'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn4',
            label: 'Кнопка',
            src: 'PerformButton',
            color: 'info'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn5',
            label: 'Кнопка',
            src: 'PerformButton',
            color: 'warning'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn6',
            label: 'Кнопка',
            src: 'PerformButton',
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
            label: 'Apple',
            src: 'PerformButton',
            icon: 'fa fa-apple'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn8',
            label: 'Github',
            src: 'PerformButton',
            icon: 'fa fa-github'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn9',
            label: 'Telegram',
            src: 'PerformButton',
            icon: 'fa fa-telegram'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn10',
            label: 'Vk',
            src: 'PerformButton',
            icon: 'fa fa-vk'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn11',
            label: 'Imdb',
            src: 'PerformButton',
            icon: 'fa fa-imdb'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn12',
            label: 'Facebook',
            src: 'PerformButton',
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
            src: 'PerformButton',
            icon: 'fa fa-apple'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn14',
            src: 'PerformButton',
            icon: 'fa fa-github'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn15',
            src: 'PerformButton',
            icon: 'fa fa-telegram'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn16',
            src: 'PerformButton',
            icon: 'fa fa-vk'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn17',
            src: 'PerformButton',
            icon: 'fa fa-imdb'
          }
        ]
      },
      {
        buttons: [
          {
            id: 'testBtn18',
            src: 'PerformButton',
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
            src: 'PerformButton',
            label: 'Большая',
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
            src: 'PerformButton',
            label: 'Стандартная'
          }
        ]
      }
    ];

    const sizeToolbarSm = [
      {
        buttons: [
          {
            id: 'small',
            src: 'PerformButton',
            label: 'Маленькая',
            size: 'sm'
          }
        ]
      }
    ];

    return (
      <React.Fragment>
        <div className="row mb-2">
          <Toolbar toolbar={toolbar1} />
        </div>
        <div className="row mb-2">
          <Toolbar toolbar={toolbar2} />
        </div>
        <div className="row mb-2">
          <Toolbar toolbar={toolbar3} />
        </div>
        <div className="row mb-2">
          <Toolbar toolbar={toolbar4} />
        </div>
        <div className="row mb-2">
          <Toolbar toolbar={toolbar5} />
        </div>
        <div className="row mb-2">
          <Toolbar toolbar={colorToolbar} />
        </div>
        <div className="row mb-2">
          <Toolbar toolbar={iconToolbar} />
        </div>
        <div className="row mb-2">
          <Toolbar toolbar={iconOnlyToolbar} />
        </div>
        <div className="row mb-2">
          <Toolbar toolbar={sizeToolbarLg} />
        </div>
        <div className="row mb-2">
          <Toolbar toolbar={sizeToolbar} />
        </div>
        <div className="row mb-2">
          <Toolbar toolbar={sizeToolbarSm} />
        </div>
      </React.Fragment>
    );
  })
  .add('Метаданные для виджета', () => (
    <Toolbar toolbar={MetaJson.toolbar.topLeft} entityKey="metaBtns" />
  ))
  .add('Ограничение доступа на кнопки', () => {
    const toolbar = [
      {
        buttons: [
          {
            id: 'testBtn22',
            label: 'Кнопка',
            src: 'PerformButton'
          },
          {
            id: 'testBtn23',
            label: 'Защищенная кнопка',
            actionId: 'dummy',
            src: 'PerformButton',
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
        <Toolbar actions={MetaJson.actions} toolbar={toolbar} entityKey="metaBtns" />
      </div>
    );
  })
  .add('Ограничение доступа на выпадающие списки', () => {
    const toolbar = [
      {
        buttons: [
          {
            id: 'testBtn22',
            label: 'Кнопка',
            src: 'DropdownButton',
            subMenu: [
              {
                id: 'testBtn23',
                label: 'Защищенная кнопка',
                actionId: 'dummy',
                src: 'PerformButton',
                security: {
                  roles: ['admin']
                }
              },
              {
                id: 'testBtn23',
                label: 'Защищенная кнопка 2',
                actionId: 'dummy',
                src: 'PerformButton',
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
        <Toolbar toolbar={toolbar} entityKey="metaBtns" />
      </div>
    );
  })
  .add('Page Buttons с предустановленным visible/disabled', () => {
    const toolbarNonVisible = [
      {
        buttons: [
          {
            id: '111',
            label: 'Кнопка',
            src: 'PerformButton',
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
            label: 'Кнопка',
            src: 'PerformButton',
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
            label: 'Дропдаун',
            src: 'DropdownButton',
            visible: false,
            subMenu: [
              {
                id: 'testBtn29',
                src: 'PerformButton',
                label: 'Элемент списка'
              },
              {
                id: 'testBtn29',
                src: 'PerformButton',
                label: 'Элемент списка'
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
            label: 'Дропдаун',
            src: 'DropdownButton',
            disabled: true,
            subMenu: [
              {
                id: 'testBtn29',
                src: 'PerformButton',
                label: 'Элемент списка'
              },
              {
                id: 'testBtn29',
                src: 'PerformButton',
                label: 'Элемент списка'
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
          <Toolbar toolbar={toolbarNonVisible} entityKey="visibleTest" />
        </div>
        <div className="row mb-2">
          Кнопка неактивна
          <Toolbar toolbar={toolbarDisabled} entityKey="disabledTest" />
        </div>
        <div className="row mb-2">
          Дропдаун не видно, потому что он скрыт
          <Toolbar toolbar={dropDownNonVisisble} entityKey="notVisibleDropDown" />
        </div>
        <div className="row mb-2">
          Дропдаун неактивен
          <Toolbar toolbar={dropDownDisabled} entityKey="dropdownDisabled" />
        </div>
      </React.Fragment>
    );
  });
