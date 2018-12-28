import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs } from '@storybook/addon-knobs/react';
import fetchMock from 'fetch-mock';
import { Route, Switch } from 'react-router-dom';

import page from './OpenPage.meta';
import Page from './Page';

const stories = storiesOf('Действия/ОpenPage', module);

stories.addDecorator(withKnobs);

stories.add('Переход по кнопке', () => {
  fetchMock.restore().get('begin:n2o/page', page);

  return (
    <React.Fragment>
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
