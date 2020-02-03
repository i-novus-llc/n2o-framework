// import React from 'react';
// import { storiesOf } from '@storybook/react';

//
// const stories = storiesOf('Ячейки/ListTextCell', module);
//
// const ListTextStory = props => {
//   return <ListTextCell {...props} />;
// };
//
// stories.add('Компонент', () => <ListTextStory />);

import React from 'react';
import { storiesOf } from '@storybook/react';
import ListTextCell from './ListTextCell';
import ListTextCellJson from './ListTextCell.meta';
import TextTableHeader from '../../headers/TextTableHeader';
import Table from '../../Table';
import Factory from '../../../../../core/factory/Factory';

const stories = storiesOf('Ячейки/TextCell', module);

stories.addParameters({
  info: {
    propTables: [ListTextCell],
    propTablesExclude: [Table, Factory],
  },
});

stories
  .add('Компонент', () => {
    const props = {
      headers: [
        {
          id: 'ListTextCell',
          component: TextTableHeader,
          label: 'ListTextCell',
        },
      ],
      cells: [
        {
          id: 'secondary',
          component: ListTextCell,
          placement: 'bottom',
          fieldKey: 'test',
          label: 'some',
          src: 'ListTextCell',
        },
      ],
      datasource: [
        {
          test: '1',
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
