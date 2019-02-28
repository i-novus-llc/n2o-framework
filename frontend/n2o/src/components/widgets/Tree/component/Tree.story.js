import React from 'react';
import { storiesOf } from '@storybook/react';
import Tree from './Tree';

const stories = storiesOf('Виджеты/Дерево', module);

stories.add('Компонент', () => {
  const datasource = [
    { id: '1', label: 'Иван' },
    { id: '2', label: 'Николай', parentId: '1' },
    { id: '3', label: 'Сергей', parentId: '2' },
    { id: '4', label: 'Сергей1', parentId: '2' }
  ];

  return <Tree datasource={datasource} />;
});
