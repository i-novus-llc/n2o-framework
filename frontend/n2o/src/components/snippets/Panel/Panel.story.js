import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, boolean, text, select } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import Factory from '../../../core/factory/Factory';
import { SNIPPETS } from '../../../core/factory/factoryLevels';

import Panel from './PanelShortHand';
import PanelJson from './Panel.meta.json';
import panelStyles from './panelStyles';

const stories = storiesOf('Регионы/Панель', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Panel'));
stories.addDecorator(jsxDecorator);
stories.addParameters({
  info: {
    propTablesExclude: [Factory],
  },
});

const tabs = [
  {
    id: '1',
    header: 'Первый таб',
    content: <h1>Контент первого таба</h1>,
  },
  {
    id: '2',
    header: 'Второй таб',
    content: <h1>Контент второго таба</h1>,
  },
];

stories
  .add('Компонент', () => {
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
      containers: PanelJson.containers,
    };

    return (
      <Panel {...props}>
        <h1>Содержимое панели</h1>
      </Panel>
    );
  })
  .add('Создание через Factory', () => {
    const dt = {
      id: 'uniqId',
      src: 'Panel',
      tabs: tabs,
      className: text('className', PanelJson.className),
      color: select('color', Object.values(panelStyles), PanelJson.color),
      icon: text('icon', PanelJson.icon),
      hasTabs: boolean('hasTabs', true),
      text: text('text', 'text'),
      headerTitle: text('headerTitle', PanelJson.headerTitle),
      footerTitle: text('footerTitle', PanelJson.footerTitle),
      open: boolean('open', PanelJson.open),
      collapsible: boolean('collapsible', PanelJson.collapsible),
      fullScreen: boolean('fullScreen', PanelJson.fullScreen),
      containers: PanelJson.containers,
    };
    return (
      <React.Fragment>
        <Factory level={SNIPPETS} id={'uniqId'} {...dt} />
      </React.Fragment>
    );
  });
