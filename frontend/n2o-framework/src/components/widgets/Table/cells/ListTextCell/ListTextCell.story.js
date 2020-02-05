import React from 'react';
import { storiesOf } from '@storybook/react';
import ListTextCell from './ListTextCell';
import ListTextCellJson from './ListTextCell.meta';
import TextTableHeader from '../../headers/TextTableHeader';
import Table from '../../Table';
import Factory from '../../../../../core/factory/Factory';
import { ListTextCell as ListTextCellComponent } from './ListTextCell';

const stories = storiesOf('Ячейки/ListTextCell', module);

stories.addParameters({
  info: {
    propTables: [ListTextCellComponent],
    propTablesExclude: [Table, Factory],
  },
});

stories
  .add(
    'Компонент',
    () => {
      return (
        <ListTextCell
          id={'testId'}
          label={'объектов'}
          fieldKey={'test'}
          model={{
            test: [
              'Первый объект',
              'Второй объект',
              'Третий объект',
              'Четвертый объект',
              'Пятый объект',
            ],
          }}
        />
      );
    },
    {
      info: {
        text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{value} объектов, текст с плейсхолдером. {value} - длина массива. Если {value} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
       />
      ~~~
      `,
      },
    }
  )
  .add(
    'LabelDashed',
    () => {
      const { id, src, fieldKey } = ListTextCellJson;
      const props = { id, src, fieldKey };
      return (
        <ListTextCell
          {...props}
          label={'объекта'}
          placement={'bottom'}
          labelDashed={true}
          model={{
            test: [
              'Первый объект',
              'Второй объект',
              'Третий объект',
              'Четвертый объект',
            ],
          }}
        />
      );
    },
    {
      info: {
        text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{value} объектов, текст с плейсхолдером. {value} - длина массива. Если {value} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
       />
      ~~~
      `,
      },
    }
  )
  .add(
    'replace placeholder',
    () => {
      const { id, src, fieldKey } = ListTextCellJson;
      const props = { id, src, fieldKey };
      return (
        <ListTextCell
          {...props}
          label={'в листе {value} объекта'}
          placement={'bottom'}
          model={{
            test: [
              'Первый объект',
              'Второй объект',
              'Третий объект',
              'Четвертый объект',
            ],
          }}
        />
      );
    },
    {
      info: {
        text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{value} объектов, текст с плейсхолдером. {value} - длина массива. Если {value} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
       />
      ~~~
      `,
      },
    }
  )
  .add(
    'hover или click',
    () => {
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
            id: 'hover',
            component: ListTextCell,
            fieldKey: 'test',
            label: 'объекта',
            src: 'ListTextCell',
            placement: 'bottom',
          },
        ],
        datasource: [
          {
            test: [
              'Первый объект',
              'Второй объект',
              'Третий объект',
              'Четвертый объект',
            ],
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
            id: 'click',
            component: ListTextCell,
            fieldKey: 'test',
            trigger: 'click',
            label: 'объекта',
            src: 'ListTextCell',
            placement: 'bottom',
          },
        ],
        datasource: [
          {
            test: ['Первый объект', 'Второй объект', 'Третий объект'],
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
    },
    {
      info: {
        text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{value} объектов, текст с плейсхолдером. {value} - длина массива. Если {value} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
       />
      ~~~
      `,
      },
    }
  )

  .add(
    'placement',
    () => {
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
            fieldKey: 'test',
            label: 'объекта',
            src: 'ListTextCell',
            placement: 'bottom',
          },
        ],
        datasource: [
          {
            test: ['Первый объект', 'Второй объект', 'Третий объект'],
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
            fieldKey: 'test',
            label: 'объекта',
            src: 'ListTextCell',
            placement: 'right',
          },
        ],
        datasource: [
          {
            test: ['Первый объект', 'Второй объект', 'Третий объект'],
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
            fieldKey: 'test',
            label: 'объекта',
            src: 'ListTextCell',
            placement: 'top',
          },
        ],
        datasource: [
          {
            test: ['Первый объект', 'Второй объект', 'Третий объект'],
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
            fieldKey: 'test',
            label: 'объекта',
            src: 'ListTextCell',
            placement: 'left',
          },
        ],
        datasource: [
          {
            test: ['Первый объект', 'Второй объект', 'Третий объект'],
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
    },
    {
      info: {
        text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{value} объектов, текст с плейсхолдером. {value} - длина массива. Если {value} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
       />
      ~~~
      `,
      },
    }
  )

  .add(
    'Метаданные',
    () => {
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
            test: ['Первый объект', 'Второй объект', 'Третий объект'],
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
    },
    {
      info: {
        text: `
      ~~~js
      import ListTextCell from 'n2o-framework/lib/components/widgets/Table/cells/ListTextCell/ListTextCell';
      
      <ListTextCell
        id="id компонента"
        src="src копонента"
        label="{value} объектов, текст с плейсхолдером. {value} - длина массива. Если {value} нет, то длина вставиться перед словом"
        fieldKey="Ключ по которому берутся данные из model"
        trigger="hover или click, default=hover"
        labelDashed="флаг underline dashed для label, default=false"
        placement="позиция tooltip (bottom, right, top, left) default=bottom"
       />
      ~~~
      `,
      },
    }
  );
