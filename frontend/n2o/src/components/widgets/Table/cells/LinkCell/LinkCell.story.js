import React from 'react';
import { storiesOf } from '@storybook/react';
import {
  withKnobs,
  text,
  boolean,
  object,
  select,
} from '@storybook/addon-knobs/react';
import { Route, Switch } from 'react-router-dom';
import Table from '../../Table';
import LinkCell from './LinkCell';
import TextTableHeader from '../../headers/TextTableHeader';
import LinkCellJson from './LinkCell.meta';
import LinkCellWithPerformJSON from './LinkCellWithPerform.meta';
import fetchMock from 'fetch-mock';
import { page } from 'N2oStorybook/fetchMock';

const stories = storiesOf('Ячейки/Ссылка', module);

stories.addDecorator(withKnobs);

stories
  .add('Метаданные', () => {
    const props = {
      id: text('id', LinkCellJson.id),
      fieldKey: boolean('animated', LinkCellJson.fieldKey),
      className: boolean('striped', LinkCellJson.className),
      action: LinkCellJson.action,
    };

    const tableProps = {
      headers: [
        {
          id: 'header',
          component: TextTableHeader,
          label: 'Обычная ссылка',
        },
      ],
      cells: [
        {
          component: LinkCell,
          ...props,
        },
      ],
      datasource: [
        {
          id: 'id',
          name: 'Ссылка на /page/widget/test',
        },
      ],
    };

    return (
      <React.Fragment>
        <Table
          headers={tableProps.headers}
          cells={tableProps.cells}
          datasource={tableProps.datasource}
        />
        <Switch>
          <Route
            path="*"
            component={({ match }) =>
              match.url !== '/' && (
                <span>
                  Сработал роутер для: <pre>{match.url}</pre>
                </span>
              )
            }
          />
        </Switch>
      </React.Fragment>
    );
  })
  .add('LinkCell с иконкой', () => {
    fetchMock.restore().get('begin:n2o/page', page);

    const tableProps = {
      headers: [
        {
          id: 'header',
          component: TextTableHeader,
          label: 'Текст и иконка',
        },
        {
          id: 'header2',
          component: TextTableHeader,
          label: 'Только иконка',
        },
      ],
      cells: [
        {
          component: LinkCell,
          ...LinkCellWithPerformJSON,
          icon: 'fa fa-pencil',
          type: 'iconAndText',
        },
        {
          component: LinkCell,
          ...LinkCellWithPerformJSON,
          icon: 'fa fa-download',
          fieldKey: 'none',
          type: 'icon',
        },
      ],
      datasource: [
        {
          id: 'id',
          name: 'Изменить',
        },
      ],
    };

    return (
      <Table
        headers={tableProps.headers}
        cells={tableProps.cells}
        datasource={tableProps.datasource}
      />
    );
  })
  .add('Отправка экшена', () => {
    fetchMock.restore().get('*', page);

    const tableProps = {
      headers: [
        {
          id: 'header',
          component: TextTableHeader,
          label: 'Отправка экшена',
        },
      ],
      cells: [
        {
          component: LinkCell,
          ...LinkCellWithPerformJSON,
          type: 'text',
        },
      ],
      datasource: [
        {
          id: 'id',
          name: 'Ссылка на открытие модального окна',
        },
      ],
    };

    return (
      <Table
        headers={tableProps.headers}
        cells={tableProps.cells}
        datasource={tableProps.datasource}
      />
    );
  })
  .add('LinkCell с action', () => {
    const tableProps = {
      headers: [
        {
          id: 'header',
          component: TextTableHeader,
          label: 'Отправка экшена',
        },
      ],
      cells: [
        {
          component: LinkCell,
          ...LinkCellWithPerformJSON,
          type: 'text',
        },
      ],
      datasource: [
        {
          id: 'id',
          name: 'Ссылка с экшеном',
        },
      ],
    };

    return (
      <Table
        headers={tableProps.headers}
        cells={tableProps.cells}
        datasource={tableProps.datasource}
      />
    );
  })
  .add('Ссылка с url и target=newWindow', () => {
    const tableProps = {
      headers: [
        {
          id: 'header',
          component: TextTableHeader,
          label: 'Отправка экшена',
        },
      ],
      cells: [
        {
          component: LinkCell,
          ...LinkCellWithPerformJSON,
          type: 'text',
          url: 'https://google.com/',
          target: 'newWindow',
        },
      ],
      datasource: [
        {
          id: 'id',
          name: 'Ссылка с url',
        },
      ],
    };

    return (
      <Table
        headers={tableProps.headers}
        cells={tableProps.cells}
        datasource={tableProps.datasource}
      />
    );
  });
