import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, boolean, text, select } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import Panel from './PanelShortHand';
import PanelJson from './Panel.meta.json';
import panelStyles from './panelStyles';

const stories = storiesOf('Регионы/Панель', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Panel'));

const tabs = [
  {
    id: '1',
    header: 'Первый таб',
    content: <h1>Контент первого таба</h1>
  },
  {
    id: '2',
    header: 'Второй таб',
    content: <h1>Контент второго таба</h1>
  }
];

stories.add('Компонент', () => {
  const props = {
    className: text('className', PanelJson.className),
    color: select('color', Object.values(panelStyles), PanelJson.color),
    icon: text('icon', PanelJson.icon),
    hasTabs: boolean('hasTabs', PanelJson.hasTabs),
    tabs: tabs,
    headerTitle: text('headerTitle', PanelJson.headerTitle),
    footerTitle: text('footerTitle', PanelJson.footerTitle),
    open: boolean('open', PanelJson.open),
    collapsible: boolean('collapsible', PanelJson.collapsible),
    fullScreen: boolean('fullScreen', PanelJson.fullScreen),
    containers: PanelJson.containers
  };

  return (
    <Panel {...props}>
      <h1>Содержимое панели</h1>
    </Panel>
  );
});
