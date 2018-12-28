import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, select } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import { list } from 'N2oStorybook/fetchMock';
import { get, values } from 'lodash';

import TextTableHeader from '../../headers/TextTableHeader';
import BadgeCell from './BadgeCell';
import Table from '../../Table';
import BadgeCellJson from './BadgeCell.meta.json';

const stories = storiesOf('Ячейки/Ярлыки', module);

stories.addDecorator(withKnobs);
// todo: баг в jest addon
stories.addDecorator(withTests('BadgeCell'));

stories
  .add('Компонент', () => {
    const props = {
      headers: [
        {
          id: 'BadgeCell',
          component: TextTableHeader,
          label: 'BadgeCell'
        }
      ],
      cells: [
        {
          id: 'secondary',
          component: BadgeCell,
          placement: select('placement', ['left', 'right'], 'left'),
          color: select(
            'color',
            ['secondary', 'primary', 'danger', 'success', 'warning', 'info'],
            'secondary'
          ),
          fieldKey: 'test',
          text: text('text', 'Мужской')
        }
      ],
      datasource: [
        {
          test: 'Иванов'
        }
      ]
    };

    return <Table headers={props.headers} cells={props.cells} datasource={props.datasource} />;
  })

  .add('Метаданные', () => {
    const badgeProps = {
      placement: select('placement', ['left', 'right'], BadgeCellJson.placement),
      color: select(
        'color',
        ['secondary', 'primary', 'danger', 'success', 'warning', 'info'],
        BadgeCellJson.color
      ),
      fieldKey: BadgeCellJson.fieldKey,
      text: BadgeCellJson.text
    };

    const props = {
      headers: [
        {
          id: 'BadgeCell',
          component: TextTableHeader,
          label: 'BadgeCell'
        }
      ],
      cells: [
        Object.assign(
          {
            id: 'secondary',
            component: BadgeCell
          },
          badgeProps
        )
      ],
      datasource: [
        {
          id: 1,
          name: 'Иван',
          gender: {
            name: 'Мужской'
          },
          status: 1
        }
      ]
    };

    return <Table headers={props.headers} cells={props.cells} datasource={props.datasource} />;
  })

  .add('Примеры', () => {
    const getProps = (placement, props) => {
      return {
        headers: [
          {
            id: 'secondary',
            component: TextTableHeader,
            label: 'secondary'
          },
          {
            id: 'primary',
            component: TextTableHeader,
            label: 'primary'
          },
          {
            id: 'danger',
            component: TextTableHeader,
            label: 'danger'
          },
          {
            id: 'warning',
            component: TextTableHeader,
            label: 'warning'
          },
          {
            id: 'info',
            component: TextTableHeader,
            label: 'info'
          },
          {
            id: 'success',
            component: TextTableHeader,
            label: 'success'
          }
        ],
        cells: [
          {
            id: 'secondary',
            component: BadgeCell,
            placement,
            ...props,
            color: 'secondary'
          },
          {
            id: 'primary',
            component: BadgeCell,
            placement,
            ...props,
            color: 'primary'
          },
          {
            id: 'danger',
            component: BadgeCell,
            placement,
            ...props,
            color: 'danger'
          },
          {
            id: 'warning',
            component: BadgeCell,
            placement,
            ...props,
            color: 'warning'
          },
          {
            id: 'info',
            component: BadgeCell,
            placement,
            ...props,
            color: 'info'
          },
          {
            id: 'success',
            component: BadgeCell,
            placement,
            ...props,
            color: 'success'
          }
        ],
        datasource: [
          {
            test: 'Иванов'
          }
        ]
      };
    };

    const rightProps = getProps('right', {
      fieldKey: 'test',
      text: 'Мужской'
    });
    const leftProps = getProps('left', {
      fieldKey: 'test',
      text: 'Женский'
    });
    const nonTextProps = getProps('right', {
      fieldKey: 'test'
    });
    return (
      <div>
        <h6>Тест слева</h6>
        <Table
          headers={rightProps.headers}
          cells={rightProps.cells}
          datasource={rightProps.datasource}
        />
        <hr />
        <h6>Тест справа</h6>
        <Table cells={leftProps.cells} datasource={leftProps.datasource} />
        <hr />
        <h6>Без текста</h6>
        <Table cells={nonTextProps.cells} datasource={nonTextProps.datasource} />
      </div>
    );
  });
