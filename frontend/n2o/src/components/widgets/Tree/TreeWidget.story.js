import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs } from '@storybook/addon-knobs/react';
import fetchMock from 'fetch-mock';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import Factory from '../../../core/factory/Factory';
import metadata from './TreeWidget.meta';

const stories = storiesOf('Виджеты/Дерево', module);
stories.addDecorator(withKnobs).add('Метаданные', () => {
  fetchMock.restore().get('begin:n2o/data', {
    list: [
      { id: '1', label: 'Система подогрева' },
      { id: '12', label: 'Обогреватель', parentId: '1' },
      { id: '13', label: 'Корпус', parentId: '1' },
      { id: '2', label: 'Система вентиляции и охлаждения' },
      { id: '21', label: 'Вентиляторы', parentId: '2' },
      { id: '22', label: 'Фильтры', parentId: '2' },
      { id: '23', label: 'Теплообменники', parentId: '2' },
      { id: '3', label: 'Аварийное охлаждение' },
      { id: '4', label: 'Система конденсации охл. жидкости' },
      { id: '41', label: 'Дренажные трубы', parentId: '4' },
      { id: '42', label: 'Отстойники', parentId: '4' },
      { id: '44', label: 'Внутренние', parentId: '42' },
      { id: '45', label: 'Внешние', parentId: '42' }
    ]
  });

  return <Factory level={WIDGETS} {...metadata['Page_Tree']} id="Page_Tree" />;
});
