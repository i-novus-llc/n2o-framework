import React from 'react';
import { connect } from 'react-redux';
import { storiesOf } from '@storybook/react';
import withTests from 'N2oStorybook/withTests';
import { set, omit, pullAt, pick } from 'lodash';

import ListRegion from './ListRegion';
import SecureListRegionJson from './ListRegion.meta';
import { metadataSuccess } from '../../../actions/pages';
import HtmlWidgetJson from '../../widgets/Html/HtmlWidget.meta';
import { userLogin, userLogout as userLogoutAction } from '../../../actions/auth';
import ListMetadata from './ListMetadata.meta';
import AuthButtonContainer from '../../../core/auth/AuthLogin';
import { makeStore } from '../../../../.storybook/decorators/utils';
import cloneObject from '../../../utils/cloneObject';
import { InitWidgetsList } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';
import { getStubData } from 'N2oStorybook/fetchMock';

const stories = storiesOf('Регионы/Лист', module);

stories.addDecorator(withTests('Лист'));
const ListRegionJson = set(
  cloneObject(SecureListRegionJson),
  'items',
  pullAt(cloneObject(SecureListRegionJson).items, 0)
);
const { store } = makeStore();

stories
  .addWithJSX('Метаданные', () => {
    store.dispatch(metadataSuccess('Page', HtmlWidgetJson));
    return <ListRegion {...ListRegionJson} pageId="Page" />;
  })
  .addWithJSX('Ограничение доступа', () => {
    store.dispatch(metadataSuccess('Page', ListMetadata));
    return (
      <div>
        <small>
          Введите <mark>admin</mark>, чтобы увидеть скрытый виджет региона
        </small>
        <AuthButtonContainer />
        <br />
        <ListRegion {...SecureListRegionJson} pageId="Page" />
      </div>
    );
  })

  .addWithJSX('Инициализация виджетов', () => {
    fetchMock
      .restore()
      .get('begin:n2o/data/test', getStubData)
      .get('begin:n2o/data2/test', async url => {
        await new Promise(r =>
          setTimeout(() => {
            r();
          }, 2000)
        );
        return getStubData(url);
      });

    store.dispatch(metadataSuccess('Page', { ...pick(InitWidgetsList, 'widgets') }));

    return <ListRegion {...omit(InitWidgetsList, 'widgets')} pageId="Page" />;
  });
