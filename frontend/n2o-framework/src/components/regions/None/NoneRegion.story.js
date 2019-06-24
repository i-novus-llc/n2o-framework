import React from 'react';
import { storiesOf } from '@storybook/react';
import withTests from 'N2oStorybook/withTests';

import NoneRegion from './NoneRegion';
import NoneRegionsJson from './NoneRegion.meta.json';
import { metadataSuccess } from '../../../actions/pages';
import HtmlWidgetJson from '../../widgets/Html/HtmlWidget.meta';
import { makeStore } from '../../../../.storybook/decorators/utils';

const stories = storiesOf('Регионы/Простой', module);

stories.addDecorator(withTests('None'));
const { store } = makeStore();
stories.add('Метаданные', () => {
  store.dispatch(metadataSuccess('Page', HtmlWidgetJson));

  return <NoneRegion {...NoneRegionsJson} pageId="Page" />;
});
