import React from 'react';
import { storiesOf } from '@storybook/react';

import CheckboxCellJson from './CheckboxCell.meta.json';
import TextTableHeader from '../../headers/TextTableHeader';
import Table from '../../Table';
import Factory from '../../../../../core/factory/Factory';
import { WIDGETS } from '../../../../../core/factory/factoryLevels';
import fetchMock from 'fetch-mock';
import withPage from 'N2oStorybook/decorators/withPage';
import CheckboxCell, {
  CheckboxCell as CheckboxCellComponent,
} from './CheckboxCell';
import set from 'lodash/set';

const stories = storiesOf('Ячейки/Чекбокс', module);

stories.addParameters({
  info: {
    propTables: [CheckboxCellComponent],
    propTablesExclude: [Table, CheckboxCell, Factory],
  },
});

const metadata = {
  Page_Table: {
    src: 'TableWidget',
    dataProvider: {
      url: 'n2o/data/test',
      pathMapping: {},
      queryMapping: {},
    },
    paging: {},
    table: {
      size: 10,
      fetchOnInit: true,
      hasSelect: true,
      className: 'n2o',
      colorFieldId: '',
      style: {},
      hasFocus: true,
      autoFocus: false,
      sorting: {
        birthday: 'ASC',
      },
      cells: [
        {
          src: 'TextCell',
          id: 'description',
        },
        {
          ...CheckboxCellJson,
          tooltipFieldId: 'tooltip',
        },
        {
          ...CheckboxCellJson,
          id: 'checkBox2',
          fieldKey: 'checkBox2',
        },
      ],
      headers: [
        {
          src: 'TextTableHeader',
          id: 'description',
          label: 'Свойства',
        },
        {
          src: 'TextTableHeader',
          id: 'checkBox',
          label: 'Отображение',
        },
        {
          src: 'TextTableHeader',
          id: 'checkBox2',
          label: 'Отображение',
        },
      ],
    },
  },
};

stories.addDecorator(withPage(metadata)).add('Метаданные', () => {
  let data = {
    list: [
      {
        id: 0,
        description: 'Чекбокс',
        checkBox: false,
        checkBox2: true,
        tooltip: 'tooltip',
      },
    ],
    count: 1,
  };

  const getStub = () => {
    return data;
  };

  const postStub = (url, xhr) => {
    const json = JSON.parse(xhr.body);
    data = set(data, `list[${json.id}].checkBox`, json.checkBox);
    return true;
  };

  fetchMock
    .restore()
    .get('begin:n2o/data', getStub)
    .post('begin:n2o/data', postStub);

  return (
    <Factory level={WIDGETS} {...metadata['Page_Table']} id="Page_Table" />
  );
});
