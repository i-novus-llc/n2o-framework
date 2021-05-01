import React from 'react';
import { storiesOf } from '@storybook/react';

import TextTableHeader from '../../headers/TextTableHeader';
import BadgeCell from './BadgeCell';
import Table from '../../Table';
import BadgeCellJson from './BadgeCell.meta.json';
import Factory from '../../../../../core/factory/Factory';

const stories = storiesOf('Ячейки/Ярлыки', module);

stories.addParameters({
  info: {
    propTables: [BadgeCell],
    propTablesExclude: [Table, Factory],
  },
});

stories
  .add('Компонент', () => {
    const props = {
      headers: [
        {
          id: 'BadgeCell',
          component: TextTableHeader,
          label: 'BadgeCell',
        },
      ],
      cells: [
        {
          id: 'secondary',
          component: BadgeCell,
          color: 'secondary',
          fieldKey: 'test',
          text: 'Мужской',
        },
      ],
      datasource: [
        {
          test: 'Иванов',
        },
      ],
    };

    return (
      <Table
        headers={props.headers}
        cells={props.cells}
        datasource={props.datasource}
      />
    );
  })
  .add('С тултипом', () => {
    const props = {
      headers: [
        {
          id: 'BadgeCell',
          component: TextTableHeader,
          label: 'BadgeCell',
        },
      ],
      cells: [
        {
          id: 'secondary',
          component: BadgeCell,
          color: 'secondary',
          fieldKey: 'test',
          text: 'Мужской',
          tooltipFieldId: 'tooltip',
        },
      ],
      datasource: [
        {
          test: 'Иванов',
          tooltip: ['tooltip', 'body'],
        },
      ],
    };

    return (
      <Table
        headers={props.headers}
        cells={props.cells}
        datasource={props.datasource}
      />
    );
  })

  .add('Метаданные', () => {
    const badgeProps = {
      placement: BadgeCellJson.placement,
      color: BadgeCellJson.color,
      fieldKey: BadgeCellJson.fieldKey,
      text: BadgeCellJson.text,
      format: BadgeCellJson.format,
      badgeFormat: BadgeCellJson.badgeFormat,
    };

    const props = {
      headers: [
        {
          id: 'BadgeCell',
          component: TextTableHeader,
          label: 'BadgeCell',
        },
      ],
      cells: [
        Object.assign(
          {
            id: 'secondary',
            component: BadgeCell,
          },
          badgeProps
        ),
      ],
      datasource: [
        {
          id: 1,
          name: 'Иван',
          my: {
            date: '01.02.2003 04:05:06',
          },
          status: 1,
        },
      ],
    };

    return (
      <Table
        headers={props.headers}
        cells={props.cells}
        datasource={props.datasource}
      />
    );
  })

  .add('Примеры', () => {
    const getProps = (placement, props) => {
      return {
        headers: [
          {
            id: 'secondary',
            component: TextTableHeader,
            label: 'secondary',
          },
          {
            id: 'primary',
            component: TextTableHeader,
            label: 'primary',
          },
          {
            id: 'danger',
            component: TextTableHeader,
            label: 'danger',
          },
          {
            id: 'warning',
            component: TextTableHeader,
            label: 'warning',
          },
          {
            id: 'info',
            component: TextTableHeader,
            label: 'info',
          },
          {
            id: 'success',
            component: TextTableHeader,
            label: 'success',
          },
        ],
        cells: [
          {
            id: 'secondary',
            component: BadgeCell,
            placement,
            ...props,
            color: 'secondary',
          },
          {
            id: 'primary',
            component: BadgeCell,
            placement,
            ...props,
            color: 'primary',
          },
          {
            id: 'danger',
            component: BadgeCell,
            placement,
            ...props,
            color: 'danger',
          },
          {
            id: 'warning',
            component: BadgeCell,
            placement,
            ...props,
            color: 'warning',
          },
          {
            id: 'info',
            component: BadgeCell,
            placement,
            ...props,
            color: 'info',
          },
          {
            id: 'success',
            component: BadgeCell,
            placement,
            ...props,
            color: 'success',
          },
        ],
        datasource: [
          {
            test: 'Иванов',
          },
        ],
      };
    };

    const rightProps = getProps('right', {
      fieldKey: 'test',
      text: 'Мужской',
    });
    const leftProps = getProps('left', {
      fieldKey: 'test',
      text: 'Женский',
    });
    const nonTextProps = getProps('right', {
      fieldKey: 'test',
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
        <Table
          cells={nonTextProps.cells}
          datasource={nonTextProps.datasource}
        />
      </div>
    );
  });
