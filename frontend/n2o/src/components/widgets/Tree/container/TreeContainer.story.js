import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number, select } from '@storybook/addon-knobs/react';
import TreeContainer from './TreeContainer';
import fetchMock from 'fetch-mock';
import { WIDGETS } from '../../../../core/factory/factoryLevels';
import Factory from '../../../../core/factory/Factory';
import metadata from './TreeWidget.meta';

const stories = storiesOf('Виджеты/Дерево', module);
stories.addDecorator(withKnobs);
stories
  .add('Данные в виде коллекции', () => {
    const datasource = [
      { id: '1', label: 'Иван' },
      { id: '2', label: 'Николай', parentId: '1' },
      { id: '3', label: 'Сергей', parentId: '2' },
      { id: '4', label: 'Сергей1', parentId: '2' },
      { id: '11', label: 'Иван' },
      { id: '12', label: 'Николай', parentId: '11' },
      { id: '13', label: 'Сергей', parentId: '12' },
      { id: '14', label: 'Сергей', parentId: '12' }
    ];

    return <TreeContainer datasource={datasource} bulkData={false} />;
  })
  .add('Данные в виде дерева', () => {
    const datasource = [
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
    ];

    return <TreeContainer datasource={datasource} bulkData={true} />;
  })
  .add('Метаданные', () => {
    fetchMock.restore().get('n2o/data/test', url => ({
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
    }));

    return <Factory level={WIDGETS} {...metadata['Page_Tree']} id="Page_Tree" />;
  });
