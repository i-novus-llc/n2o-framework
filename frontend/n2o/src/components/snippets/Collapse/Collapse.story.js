import React from 'react';
import { setDisplayName } from 'recompose';
import { storiesOf } from '@storybook/react';
import {
  withKnobs,
  text,
  boolean,
  select,
  object,
} from '@storybook/addon-knobs/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import Collapse, { Panel } from './Collapse';
import InputSelect from '../../controls/InputSelect/InputSelect';
import InputSelectJson from '../../controls/InputSelect/InputSelect.meta';
import { SNIPPETS } from '../../../core/factory/factoryLevels';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('UI Компоненты/Collapse', module);

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);

const textToPanel =
  'Значимость этих проблем настолько очевидна, что сложившаяся структура организации требует определения и уточнения модели развития! Дорогие друзья, рамки и место обучения кадров создаёт предпосылки качественно новых шагов для дальнейших направлений развития проекта! Дорогие друзья, курс на социально-ориентированный национальный проект представляет собой интересный эксперимент проверки дальнейших направлений развития проекта.';

const CollapseToStory = setDisplayName('Collapse')(props => (
  <Collapse defaultActiveKey="1">
    <Panel key="1" header="Первый" {...props}>
      {textToPanel}
    </Panel>
    <Panel key="2" header="Второй" {...props}>
      {textToPanel}
    </Panel>
    <Panel key="3" header="Третий" {...props}>
      {textToPanel}
    </Panel>
  </Collapse>
));

stories.addParameters({
  info: {
    propTables: [Collapse, Panel],
    propTablesExclude: [CollapseToStory, InputSelect],
  },
});

stories
  .add('Компонент type=default', () => {
    const props = {
      type: select('type', ['default', 'line', 'divider'], 'default'),
      activeKey: text('activeKey', ''),
      defaultActiveKey: text('defaultActiveKey', ''),
      destroyInactivePanel: boolean('destroyInactivePanel', false),
      accordion: boolean('accordion', false),
    };

    return <CollapseToStory {...props} />;
  })
  .add('Компонент type=line', () => {
    return <CollapseToStory type="line" />;
  })
  .add('Компонент type=divider', () => {
    return <CollapseToStory type="divider" />;
  })
  .add('Компонент с InputSelect внутри', () => {
    return (
      <Collapse defaultActiveKey="1">
        <Panel key="1" header="Первый">
          <InputSelect {...InputSelectJson} options={InputSelectJson.value} />
        </Panel>
        <Panel key="2" header="Второй">
          <InputSelect {...InputSelectJson} options={InputSelectJson.value} />
        </Panel>
      </Collapse>
    );
  })
  .add('Компонент с длинным заголовком', () => {
    return (
      <Collapse defaultActiveKey="1">
        <Panel
          key="1"
          type="default"
          header="Lorem ipsum dolor sit amet, consectetur adipisicing elit. Blanditiis corporis ex expedita magnam quibusdam quisquam quod rerum. Assumenda deleniti, earum ipsa porro praesentium qui quisquam quod repudiandae sequi tempore totam."
        >
          {textToPanel}
        </Panel>
        <Panel
          key="2"
          type="line"
          header="Lorem ipsum dolor sit amet, consectetur adipisicing elit. Blanditiis corporis ex expedita magnam quibusdam quisquam quod rerum. Assumenda deleniti, earum ipsa porro praesentium qui quisquam quod repudiandae sequi tempore totam."
        >
          {textToPanel}
        </Panel>
        <Panel
          key="3"
          type="divider"
          header="Lorem ipsum dolor sit amet, consectetur adipisicing elit. Blanditiis corporis ex expedita magnam quibusdam quisquam quod rerum. Assumenda deleniti, earum ipsa porro praesentium qui quisquam quod repudiandae sequi tempore totam."
        >
          {textToPanel}
        </Panel>
        <Panel
          key="4"
          type="divider"
          header="Lorem ipsum dolor sit amet, consectetur adipisicing elit. Blanditiis corporis ex expedita magnam quibusdam quisquam quod rerum. Assumenda deleniti, earum ipsa porro praesentium qui quisquam quod repudiandae sequi tempore totam."
        >
          {textToPanel}
        </Panel>
      </Collapse>
    );
  })
  .add('Создание через Factory', () => {
    const dt = {
      id: 'uniqId',
      src: 'Collapse',
      type: select('type', ['default', 'line', 'divider'], 'default'),
      defaultActiveKey: text('defaultActiveKey', '1'),
      destroyInactivePanel: boolean('destroyInactivePanel', false),
      accordion: boolean('accordion', false),
      dataKey: text('dataKey', 'items'),
      items: object('items', [
        { key: '1', header: 'Первый', text: 'Teкст' },
        { key: '2', header: 'Второй', text: 'Teкст' },
      ]),
    };
    return (
      <React.Fragment>
        <Factory level={SNIPPETS} id={'uniqId'} {...dt} />
      </React.Fragment>
    );
  });
