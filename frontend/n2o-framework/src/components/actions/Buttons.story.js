import React from 'react';
import { storiesOf } from '@storybook/react';

import { getStubData } from 'N2oStorybook/fetchMock';
import { filterMetadata } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';

import metadata from './ButtonDependency.meta.json';
import metadata2 from './ButtonDependencyDisableButton.meta.json';
import Factory from '../../core/factory/Factory';
import { WIDGETS } from '../../core/factory/factoryLevels';
import withPage from '../../../.storybook/decorators/withPage';

const stories = storiesOf(
  'Функциональность/Зависимость кнопок от модели',
  module
);

stories
  .addDecorator(withPage(metadata))

  .add('Метаданные', () => {
    fetchMock.restore().get('begin:n2o/data', url => {
      return { ...getStubData(url), list: getStubData(url).list.slice(0, 3) };
    });
    return (
      <div>
        <Factory level={WIDGETS} {...metadata['Page_Table']} id="Page_Table" />
      </div>
    );
  })
  .add('Метаданные disabled button', () => {
    fetchMock.restore().get('begin:n2o/data', url => {
      return { ...getStubData(url), list: getStubData(url).list.slice(0, 3) };
    });
    return (
      <div>
        <Factory level={WIDGETS} {...metadata2['Page_Table']} id="Page_Table" />
      </div>
    );
  });
