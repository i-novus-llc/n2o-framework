import React from 'react';
import { Route, Link, Switch } from 'react-router-dom';

import { storiesOf } from '@storybook/react';
import fetchMock from 'fetch-mock';
import { getStubData } from 'N2oStorybook/fetchMock';

import Page from './Page';

import metadataWelcome from './json/WelcomeRouterPage.meta';
import metadataStaff from './json/StaffRouterPage.meta';

storiesOf('Роутинг', module).add('Проверки mapping', () => {
  fetchMock.restore().get('begin:n2o/page', url => {
    switch (url) {
      case 'n2o/page/':
        return metadataWelcome;
      case 'n2o/page/staff':
      case 'n2o/page/staff/organizations/5':
        return metadataStaff;
      default:
        return {};
    }
  });
  fetchMock.get('begin:n2o/data', getStubData);
  return (
    <div>
      <div className="row">
        <div className="col-6">
          <h5>Меню</h5>
          <div className="nav flex-column">
            <Link className="nav-link" to="/">
              Welcome
            </Link>
            <Link className="nav-link" to="/staff">
              Страница
            </Link>
            <Link className="nav-link" to="/staff/organizations/5">
              Проверка pathMapping
            </Link>
            <Link className="nav-link" to="/staff?name=Тест">
              Проверка queryMapping
            </Link>
          </div>
        </div>
        <div className="col-6">
          <Switch>
            <Route
              path="/:pageId?"
              render={props => (
                <React.Fragment>
                  <div style={{ textAlign: 'right' }}>
                    URL:{' '}
                    <kbd>
                      {props.location.pathname}
                      {props.location.search}
                    </kbd>
                  </div>
                  <Page autoPage {...props} />
                </React.Fragment>
              )}
            />
          </Switch>
        </div>
      </div>
    </div>
  );
});
