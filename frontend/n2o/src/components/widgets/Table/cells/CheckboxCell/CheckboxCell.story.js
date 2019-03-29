import React from 'react';
import { storiesOf } from '@storybook/react';
import { boolean, withKnobs } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import CheckboxCellJson from './CheckboxCell.meta.json';
import TextTableHeader from '../../headers/TextTableHeader';
import Table from '../../Table';
import Factory from '../../../../../core/factory/Factory';
import { WIDGETS } from '../../../../../core/factory/factoryLevels';
import { getStubData } from 'N2oStorybook/fetchMock';
import fetchMock from 'fetch-mock';
import withPage from 'N2oStorybook/decorators/withPage';
import CheckboxCell from './CheckboxCell';
import { set } from 'lodash';

const stories = storiesOf('Ячейки/Чекбокс', module);

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

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('CheckboxCell'));

stories.addDecorator(withPage(metadata)).add('Метаданные', () => {
  let data = {
    list: [{ id: 0, description: 'Чекбокс', checkBox: false, checkBox2: true }],
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
