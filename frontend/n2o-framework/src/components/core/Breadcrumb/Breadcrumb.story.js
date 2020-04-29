import React from 'react';
import { withContext } from 'recompose';
import { Route, Link, Switch, BrowserRouter } from 'react-router-dom';
import fetchMock from 'fetch-mock';
import { storiesOf } from '@storybook/react';

import { getStubData } from 'N2oStorybook/fetchMock';
import DefaultBreadcrumb from './DefaultBreadcrumb';
import metadata from '../Page.meta';
import Page from '../Page';
import PropTypes from 'prop-types';
import { PlaceholderBreadCrumb } from 'N2oStorybook/json';
import page from '../OpenPage.meta';

const stories = storiesOf('Функциональность/Хлебные крошки', module);

const PageContext = withContext(
  {
    defaultBreadcrumb: PropTypes.func,
  },
  props => ({
    defaultBreadcrumb: DefaultBreadcrumb,
  })
)(Page);

stories
  .add('Метаданные', () => {
    const withForward = JSON.parse(JSON.stringify(metadata));
    withForward.id = 'OtherPage';
    withForward.widget.toolbar.topLeft[0].buttons[0].title = 'Назад';
    withForward.widget.toolbar.topLeft[0].buttons[0].id = 'back';
    withForward.widget.actions.redirect.options.path = '/';

    fetchMock.restore().get('begin:n2o/page', url => {
      if (url === 'n2o/page/Page') {
        return { ...metadata, breadcrumb: metadata.breadcrumb.slice(0, 1) };
      }
      return withForward;
    });

    fetchMock.get('*', getStubData);

    return (
      <BrowserRouter>
        <Switch>
          <Route
            path="/test"
            exact
            component={() => (
              <PageContext
                pageId="OtherPage"
                pageUrl="OtherPage"
                metadata={metadata}
              />
            )}
          />
          <Route
            path="/"
            component={() => (
              <PageContext
                pageId="testSimplePageJson"
                pageUrl="Page"
                metadata={metadata}
              />
            )}
          />
        </Switch>
      </BrowserRouter>
    );
  })
  .add('Плейсхолдер', () => {
    fetchMock.restore().get('*', url => {
      return PlaceholderBreadCrumb;
    });
    return (
      <PageContext
        pageId="testSimplePageJson"
        pageUrl="Page"
        metadata={metadata}
      />
    );
  })
  .add('без props path', () => {
    return <DefaultBreadcrumb items={metadata.breadcrumb} />;
  });
