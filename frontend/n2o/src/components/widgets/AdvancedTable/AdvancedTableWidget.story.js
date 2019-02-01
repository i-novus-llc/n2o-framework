import React from 'react';
import { storiesOf } from '@storybook/react';
import { getStubData } from 'N2oStorybook/fetchMock';
import { filterMetadata, newEntry } from 'N2oStorybook/json';
import fetchMock from 'fetch-mock';
import { set } from 'lodash';
import metadata from './AdvancedTableWidget.meta';
import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import { START_INVOKE } from '../../../constants/actionImpls';
import { omit } from 'lodash';
import { id } from '../../../utils/id';
import cloneObject from '../../../utils/cloneObject';
import AuthButtonContainer from '../../../core/auth/AuthLogin';
import withPage from '../../../../.storybook/decorators/withPage';

const stories = storiesOf('Виджеты/Advanced Table', module);

const urlPattern = 'begin:n2o/data';

stories.addDecorator(withPage(metadata)).add('Метаданные', () => {
  fetchMock.restore().get(urlPattern, url => getStubData(url));

  return <Factory level={WIDGETS} {...metadata['Page_Table']} id="Page_Table" />;
});
