import React from 'react';
import { storiesOf } from '@storybook/react';
import { getStubData } from 'N2oStorybook/fetchMock';
import fetchMock from 'fetch-mock';

import Html from './Html';
import HtmlJson from './Html.meta.json';

const stories = storiesOf('Виджеты/HtmlWidget', module);

stories.add('Метаданные', () => {
  const delay = () => new Promise((res, rej) => setTimeout(res, 1000));

  fetchMock.restore().mock('begin:/html-data', delay().then(() => '<h1>Ответ с сервера</h1>'));

  return <Html {...HtmlJson} />;
});
