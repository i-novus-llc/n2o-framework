import React, { Fragment } from 'react';
import { map } from 'lodash';
import { storiesOf } from '@storybook/react';
import { number, text, withKnobs, select, boolean } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import Table from '../../Table';
import TextTableHeader from '../../headers/TextTableHeader';
import TextCell from '../TextCell/TextCell';
import ButtonsCell from './ButtonsCell';
import metadata from './ButtonsCell.meta';

const stories = storiesOf('Ячейки/ButtonsCell', module);

const examplesDataSource = [
  {
    description: 'Кнопки',
    buttons: [
      {
        id: '1',
        label: 'Первая',
        hint: 'Первая',
        size: 'md',
        color: 'primary'
      },
      {
        id: '2',
        label: 'Вторая',
        hint: 'Вторая',
        size: 'md',
        color: 'secondary'
      },
      {
        id: '3',
        label: 'Третья',
        hint: 'Третья',
        size: 'md',
        color: 'warning'
      }
    ]
  },
  {
    description: 'Кнопки в виде ссылок',

    buttons: [
      {
        id: '4',
        label: 'Первая',
        hint: 'Первая',
        size: 'md'
      },
      {
        id: '5',
        label: 'Вторая',
        hint: 'Вторая',
        size: 'md'
      },
      {
        id: '6',
        label: 'Третья',
        hint: 'Третья',
        size: 'md'
      }
    ]
  },
  {
    description: 'Кнопки с иконками',

    buttons: [
      {
        id: '7',
        label: 'Первая',
        hint: 'Первая',
        size: 'md',
        icon: 'fa fa-telegram',
        color: 'primary'
      },
      {
        id: '8',
        label: 'Вторая',
        hint: 'Вторая',
        size: 'md',
        icon: 'fa fa-vk',
        color: 'secondary'
      },
      {
        id: '9',
        label: 'Третья',
        hint: 'Третья',
        size: 'md',
        icon: 'fa fa-facebook',
        color: 'warning'
      }
    ]
  },
  {
    description: 'Кнопки в виде ссылок с иконками',

    buttons: [
      {
        id: '10',
        label: 'Первая',
        hint: 'Первая',
        icon: 'fa fa-telegram',
        size: 'md'
      },
      {
        id: '11',
        label: 'Вторая',
        hint: 'Вторая',
        icon: 'fa fa-vk',
        size: 'md'
      },
      {
        id: '12',
        label: 'Третья',
        hint: 'Третья',
        icon: 'fa fa-facebook',
        size: 'md'
      }
    ]
  },
  {
    description: 'Кнопка с выпадающим списком',

    buttons: [
      {
        id: '13',
        label: 'Первая',
        icon: '',
        hint: 'Первая',
        color: 'primary'
      },
      {
        id: '14',
        label: 'Вторая',
        icon: '',
        hint: 'Вторая',
        color: 'danger',
        subMenu: [
          {
            id: '15',
            label: 'Первый',
            icon: ''
          },
          {
            id: '16',
            label: 'Второй',
            icon: ''
          }
        ]
      }
    ]
  },
  {
    description: 'Кнопки в виде ссылок с выпадающим списком',
    buttons: [
      {
        id: '17',
        label: 'Первая',
        icon: '',
        hint: 'Первая'
      },
      {
        id: '18',
        label: 'Вторая',
        icon: '',
        hint: 'Вторая',
        subMenu: [
          {
            id: '19',
            label: 'Первый',
            icon: ''
          },
          {
            id: '20',
            label: 'Второй'
          }
        ]
      },
      {
        id: '21',
        label: 'Третья',
        icon: 'fa fa-vk',
        hint: 'Третья'
      }
    ]
  },
  {
    description: 'Использование разделителей, заголовок и иконок в выпадающем списке',
    buttons: [
      {
        id: '22',
        label: 'Вторая',
        icon: 'fa fa-vk',
        hint: 'Вторая',
        subMenu: [
          {
            id: '23',
            header: true,
            label: 'Заголовок'
          },
          {
            id: '24',
            divider: true
          },
          {
            id: '25',
            label: 'С иконкой',
            icon: 'fa fa-telegram'
          }
        ]
      }
    ]
  },
  {
    description: 'Вызов действия',
    buttons: [
      {
        id: '26',
        label: 'Действие',
        icon: 'fa fa-vk',
        subMenu: [
          {
            id: '27',
            label: 'Модальное окно',
            header: true
          },
          {
            id: '28',
            divider: true
          },
          {
            id: '29',
            label: 'Открыть',
            action: {
              src: 'perform',
              options: {
                type: 'n2o/modals/INSERT',
                payload: {
                  pageUrl: '/page/widget/create',
                  size: 'sm',
                  visible: true,
                  closeButton: true,
                  title: 'Новое модальное окно',
                  pageId: 'undefined'
                }
              }
            }
          }
        ]
      }
    ]
  }
];

const createTable = data =>
  map(data, ({ buttons, description }) => {
    const tableProps = {
      cells: [
        {
          component: TextCell,
          id: 'description',
          style: { width: '200px' }
        },
        {
          component: ButtonsCell,
          id: 'buttonCells',
          fieldKey: 'buttonCells',
          buttons
        }
      ],
      headers: [
        {
          component: TextTableHeader,
          id: 'description',
          label: 'Свойства'
        },
        {
          component: TextTableHeader,
          id: 'buttonCells',
          label: 'Отображение'
        }
      ],
      datasource: [{ description }]
    };

    return <Table {...tableProps} />;
  });

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('CheckboxCell'));

stories
  .add('Метаданные', () => {
    const props = {
      fieldKey: text('fieldKey', metadata.fieldKey),
      id: text('id', metadata.buttons[0].id),
      className: text('className', metadata.className),
      buttons: [
        {
          label: text('label', metadata.buttons[0].label),
          icon: text('color', metadata.buttons[0].icon),
          hint: text('hint', metadata.buttons[0].hint),
          size: select('size', ['lg', 'md', 'sm'], metadata.buttons[0].size),
          visible: boolean('visible', metadata.buttons[0].visible),
          disabled: boolean('disabled', metadata.buttons[0].disabled),
          color: text('color', metadata.buttons[0].color),
          action: metadata.buttons[0].action
        }
      ]
    };

    return createTable([
      {
        description: 'Кнопки',
        buttons: props.buttons
      }
    ]);
  })
  .add('Примеры', () => {
    return <div style={{ paddingBottom: 50 }}>{createTable(examplesDataSource)}</div>;
  });
