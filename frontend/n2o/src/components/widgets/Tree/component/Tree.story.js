import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number, select } from '@storybook/addon-knobs/react';
import Tree from './Tree';

const stories = storiesOf('Виджеты/Дерево', module);
stories.addDecorator(withKnobs);
stories.add('Компонент', () => {
  const props = {
    disabled: boolean('disabled', false),
    loading: boolean('loading', false),
    parentFieldId: text('parentFieldId', 'parentId'),
    valueFieldId: text('valueFieldId', 'id'),
    labelFieldId: text('label', 'label'),
    iconFieldId: text('iconFieldId', 'icon'),
    imageFieldId: text('imageFieldId', 'image'),
    badgeFieldId: text('badgeFieldId', 'badge'),
    badgeColorFieldId: text('badgeColorFieldId', 'color'),
    hasCheckboxes: boolean('hasCheckboxes', false),
    parentIcon: text('parentIcon', ''),
    childIcon: text('childIcon', ''),
    draggable: boolean('draggable', true),
    multiselect: boolean('multiselect', false),
    showLine: boolean('showLine', false),
    filter: select('filter', ['includes', 'startsWith', 'endsWith', '-'], '-'),
    expandBtn: boolean('expandBtn', false)
  };

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

  return <Tree datasource={datasource} {...props} />;
});
