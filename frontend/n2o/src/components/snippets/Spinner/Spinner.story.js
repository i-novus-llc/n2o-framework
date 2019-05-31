import React, { Fragment } from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import {
  withKnobs,
  text,
  boolean,
  number,
  array,
  select,
} from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import Input from '../../controls/Input/Input';
import InlineSpinner from './InlineSpinner';
import TextCell from '../../widgets/Table/cells/TextCell/TextCell';
import TextTableHeader from '../../widgets/Table/headers/TextTableHeader';
import Table from '../../widgets/Table/Table';
import CoverSpinner from './CoverSpinner';
import Spinner from './Spinner';

const stories = storiesOf('UI Компоненты/Спиннеры', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Spinner'));
const tableData = [
  { id: '1', name: 'Foo', surname: 'Bar', birthday: '01.01.2001' },
  { id: '2', name: 'X', surname: 'Y', birthday: '01.01.1001' },
  { id: '3', name: 'Test', surname: 'Tset', birthday: '01.01.0001' },
];

stories
  .add('Базовый функционал', () => {
    return (
      <div>
        <div style={{ display: 'flex' }}>
          <Input />
          <InlineSpinner />
        </div>
        <div style={{ marginTop: 50, position: 'relative' }}>
          <Table>
            <Table.Header>
              <Table.Row>
                <Table.Cell
                  as="th"
                  component={TextTableHeader}
                  id="name"
                  sortable={false}
                  label="Имя"
                />
                <Table.Cell
                  as="th"
                  component={TextTableHeader}
                  id="surname"
                  sortable={false}
                  label="Фамилия"
                />
                <Table.Cell
                  as="th"
                  component={TextTableHeader}
                  id="birthday"
                  sortable={false}
                  label="Дата рождения"
                />
              </Table.Row>
            </Table.Header>
            <Table.Body>
              {tableData.map(data => (
                <Table.Row>
                  <Table.Cell
                    component={TextCell}
                    model={data}
                    id="name"
                    fieldKey="name"
                  />
                  <Table.Cell id="surname">
                    <TextCell model={data} fieldKey="surname" />
                  </Table.Cell>
                  <Table.Cell
                    component={TextCell}
                    model={data}
                    id="birthday"
                    fieldKey="birthday"
                  />
                </Table.Row>
              ))}
            </Table.Body>
          </Table>
          <CoverSpinner message="Таблица загружается..." />
        </div>
      </div>
    );
  })
  .add('Компонент', () => {
    const props = {
      loading: boolean('loading', true),
      type: select('type', ['cover', 'inline'], 'inline'),
      text: text('text', 'text'),
      delay: select('delay', [1000, 2000, 3000, 4000], 1000),
      mode: select('mode', ['dark', 'light', 'transparent'], 'dark'),
    };
    return (
      <Fragment>
        <Spinner {...props} />
      </Fragment>
    );
  });
