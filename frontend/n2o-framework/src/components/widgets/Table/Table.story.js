import React from 'react';
import { storiesOf } from '@storybook/react';

import Table from './Table';
import TextTableHeader from './headers/TextTableHeader';
import TextCell from './cells/TextCell/TextCell';

const stories = storiesOf('Виджеты/Таблица', module);

stories.add('Компонент', () => {
  const tableData = [
    { id: '1', name: 'Иван', surname: 'Петров', birthday: '01.01.2001' },
    { id: '2', name: 'Николай', surname: 'Киреев', birthday: '08.03.1991' },
    { id: '3', name: 'Сергей', surname: 'Шатонов', birthday: '11.11.2011' },
  ];

  return (
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
  );
});
