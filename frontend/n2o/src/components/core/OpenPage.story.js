import React from 'react';
import { storiesOf } from '@storybook/react';
import fetchMock from 'fetch-mock';
import { Route, Switch, Link } from 'react-router-dom';

import page from './OpenPage.meta';
import Page from './Page';

const stories = storiesOf('Действия/ОpenPage', module);

stories.add('Переход по кнопке', () => {
  fetchMock.restore().get('begin:n2o/page', page);

  return (
    <React.Fragment>
      <Link to="/test">Ссылка</Link>
      <Page pageId="testSimplePageJson" pageUrl="testSimplePageJson" />
      <Switch>
        <Route
          path="/:input"
          component={({ match }) => <h3>Сработал роутер для: {match.url}</h3>}
        />
      </Switch>
    </React.Fragment>
  );
});
