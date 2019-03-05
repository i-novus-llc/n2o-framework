import React from 'react';
import { storiesOf } from '@storybook/react';
import withTests from 'N2oStorybook/withTests';
import { getStubData } from 'N2oStorybook/fetchMock';
import { filterMetadata, newEntry, tableActions } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import withPage from '../../../../.storybook/decorators/withPage';
import { page } from 'N2oStorybook/fetchMock';
import metadata from './List.meta';

const stories = storiesOf('Виджеты/Лист', module);

const urlPattern = 'begin:n2o/data';

stories.addDecorator(withPage(metadata)).add('Компонент', () => {
  fetchMock.restore().get(urlPattern, url => getStubData(url));

  return <Factory level={WIDGETS} {...metadata['List']} id="List" />;
});
