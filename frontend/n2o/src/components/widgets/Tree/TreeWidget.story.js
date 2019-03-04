import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number, select } from '@storybook/addon-knobs/react';
import fetchMock from 'fetch-mock';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import Factory from '../../../core/factory/Factory';
import metadata from './TreeWidget.meta';

const stories = storiesOf('Виджеты/Дерево', module);
stories.addDecorator(withKnobs).add('Метаданные', () => {
  fetchMock.restore().get('begin:n2o/data', {
    list: [
      { id: '1', label: 'Иван' },
      {
        id: '2',
        label: 'Николай',
        children: [{ id: '3', label: 'Сергей' }, { id: '4', label: 'Сергей1' }]
      },
      {
        id: '12',
        label: 'Николай',
        children: [{ id: '123', label: 'Cthu' }]
      }
    ]
  });

  return <Factory level={WIDGETS} {...metadata['Page_Tree']} id="Page_Tree" />;
});
