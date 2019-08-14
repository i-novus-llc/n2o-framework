import React from 'react';
import { storiesOf } from '@storybook/react';

import { getStubData } from 'N2oStorybook/fetchMock';
import { filterMetadata, meta } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';

import Factory from '../core/factory/Factory';
import { WIDGETS } from '../core/factory/factoryLevels';
import withPage from '../../.storybook/decorators/withPage';

const stories = storiesOf('Функциональность/Сайд-эффекты на экшены', module);

stories
  .addDecorator(withPage(meta))

  .add('Метаданные', () => {
    fetchMock.restore().get('begin:n2o/data', url => {
      return getStubData(url);
    });
    fetchMock.post('begin:n2o/data', getStubData);
    return (
      <div>
        <Factory level={WIDGETS} id="Page_Table" {...meta['Page_Table']} />
      </div>
    );
  });
