import React from 'react';
import { storiesOf } from '@storybook/react';
import ListTextCell from './ListTextCell';
import ListTextCellJson from './ListTextCell.meta';
import TextTableHeader from '../../headers/TextTableHeader';
import Table from '../../Table';
import Factory from '../../../../../core/factory/Factory';

const stories = storiesOf('Ячейки/ListTextCell', module);

stories.addParameters({
  info: {
    propTables: [ListTextCell],
    propTablesExclude: [Table, Factory],
  },
});

stories
  .add('Компонент', () => {
    return (
      <ListTextCell
        label={'объектов'}
        fieldKey={[
          'Первый объект',
          'Второй объект',
          'Третий объект',
          'Четвертый объект',
          'Пятый объект',
        ]}
      />
    );
  })
  .add('LabelDashed', () => {
    const { id, src, fieldKey } = ListTextCellJson;
    const props = { id, src, fieldKey };
    return (
      <ListTextCell
        {...props}
        label={'объекта'}
        placement={'bottom'}
        labelDashed={true}
      />
    );
  })
  .add('replace placeholder', () => {
    const { id, src, fieldKey } = ListTextCellJson;
    const props = { id, src, fieldKey };
    return (
      <ListTextCell
        {...props}
        label={'в листе {value} объекта'}
        placement={'bottom'}
      />
    );
  })
  .add('hover или click', () => {
    const props = {
      headers: [
        {
          id: 'ListTextCell',
          component: TextTableHeader,
          label: 'hover',
        },
      ],
      cells: [
        {
          id: 'secondary',
          component: ListTextCell,
          fieldKey: [
            'Первый объект',
            'Второй объект',
            'Третий объект',
            'Четвертый объект',
          ],
          label: 'объекта',
          src: 'ListTextCell',
          placement: 'bottom',
        },
      ],
      datasource: [
        {
          test: '1',
        },
      ],
    };
    const props2 = {
      headers: [
        {
          id: 'ListTextCell',
          component: TextTableHeader,
          label: 'click',
        },
      ],
      cells: [
        {
          id: 'secondary',
          component: ListTextCell,
          fieldKey: ['Первый объект', 'Второй объект', 'Третий объект'],
          trigger: 'click',
          label: 'объекта',
          src: 'ListTextCell',
          placement: 'bottom',
        },
      ],
      datasource: [
        {
          test: '1',
        },
      ],
    };
    return (
      <div
        style={{
          display: 'flex',
          width: '250px',
          justifyContent: 'space-between',
        }}
      >
        <Table
          headers={props.headers}
          cells={props.cells}
          datasource={props.datasource}
        />
        <Table
          headers={props2.headers}
          cells={props2.cells}
          datasource={props2.datasource}
        />
      </div>
    );
  })

  .add('placement', () => {
    const props = {
      headers: [
        {
          id: 'ListTextCell',
          component: TextTableHeader,
          label: 'bottom',
        },
      ],
      cells: [
        {
          id: 'secondary',
          component: ListTextCell,
          fieldKey: ['Первый объект', 'Второй объект', 'Третий объект'],
          label: 'объекта',
          src: 'ListTextCell',
          placement: 'bottom',
        },
      ],
      datasource: [
        {
          test: '1',
        },
      ],
    };
    const props2 = {
      headers: [
        {
          id: 'ListTextCell',
          component: TextTableHeader,
          label: 'right',
        },
      ],
      cells: [
        {
          id: 'secondary',
          component: ListTextCell,
          fieldKey: ['Первый объект', 'Второй объект', 'Третий объект'],
          label: 'объекта',
          src: 'ListTextCell',
          placement: 'right',
        },
      ],
      datasource: [
        {
          test: '1',
        },
      ],
    };
    const props3 = {
      headers: [
        {
          id: 'ListTextCell',
          component: TextTableHeader,
          label: 'top',
        },
      ],
      cells: [
        {
          id: 'secondary',
          component: ListTextCell,
          fieldKey: ['Первый объект', 'Второй объект', 'Третий объект'],
          label: 'объекта',
          src: 'ListTextCell',
          placement: 'top',
        },
      ],
      datasource: [
        {
          test: '1',
        },
      ],
    };
    const props4 = {
      headers: [
        {
          id: 'ListTextCell',
          component: TextTableHeader,
          label: 'left',
        },
      ],
      cells: [
        {
          id: 'secondary',
          component: ListTextCell,
          fieldKey: ['Первый объект', 'Второй объект', 'Третий объект'],
          label: 'объекта',
          src: 'ListTextCell',
          placement: 'left',
        },
      ],
      datasource: [
        {
          test: '1',
        },
      ],
    };
    return (
      <div
        style={{
          display: 'flex',
          width: '750px',
          justifyContent: 'space-between',
        }}
      >
        <Table
          headers={props.headers}
          cells={props.cells}
          datasource={props.datasource}
        />
        <Table
          headers={props2.headers}
          cells={props2.cells}
          datasource={props2.datasource}
        />
        <Table
          headers={props3.headers}
          cells={props3.cells}
          datasource={props3.datasource}
        />
        <Table
          headers={props4.headers}
          cells={props4.cells}
          datasource={props4.datasource}
        />
      </div>
    );
  })

  .add('Метаданные', () => {
    const ListTextCellProps = {
      placement: ListTextCellJson.placement,
      fieldKey: ListTextCellJson.fieldKey,
      label: ListTextCellJson.label,
      id: ListTextCellJson.id,
      src: ListTextCellJson.id,
      trigger: ListTextCellJson.trigger,
    };

    const props = {
      headers: [
        {
          id: 'LinkTextCell',
          component: TextTableHeader,
          label: 'LinkTextCell',
        },
      ],
      cells: [
        Object.assign(
          {
            id: 'secondary',
            component: ListTextCell,
          },
          ListTextCellProps
        ),
      ],
      datasource: [
        {
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
  });
