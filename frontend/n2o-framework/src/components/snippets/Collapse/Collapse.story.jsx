import React from 'react'
import { setDisplayName } from 'recompose'
import { storiesOf } from '@storybook/react'

import InputSelect from '../../controls/InputSelect/InputSelect'
import InputSelectJson from '../../controls/InputSelect/InputSelect.meta'
import { SNIPPETS } from '../../../core/factory/factoryLevels'
import Factory from '../../../core/factory/Factory'

import Collapse, { Panel } from './Collapse'

const stories = storiesOf('UI Компоненты/Collapse', module)

const textToPanel =
  'Значимость этих проблем настолько очевидна, что сложившаяся структура организации требует определения и уточнения модели развития! Дорогие друзья, рамки и место обучения кадров создаёт предпосылки качественно новых шагов для дальнейших направлений развития проекта! Дорогие друзья, курс на социально-ориентированный национальный проект представляет собой интересный эксперимент проверки дальнейших направлений развития проекта.'

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
))

stories.addParameters({
    info: {
        propTables: [Collapse, Panel],
        propTablesExclude: [CollapseToStory, InputSelect],
    },
})

stories
    .add(
        'Компонент type=default',
        () => {
            const props = {
                type: 'default',
                activeKey: '',
                defaultActiveKey: '',
                destroyInactivePanel: false,
                accordion: false,
            }

            return <CollapseToStory {...props} />
        },
        {
            info: {
                text: `
      Компонент 'Сворачиваемый список'
      ~~~js
      import Collapse, { Panel } from 'n2o-framework/lib/components/snippets/Collapse/Collapse';
      
      <Collapse defaultActiveKey="1">
        <Panel key="1" header="Первый" type="default" >{textToPanel}</Panel>
        <Panel key="2" header="Второй" type="default" >{textToPanel}</Panel>
        <Panel key="3" header="Третий" type="default" >{textToPanel}</Panel>
      </Collapse>
      ~~~
      `,
            },
        },
    )
    .add(
        'Компонент type=line',
        () => <CollapseToStory type="line" />,
        {
            info: {
                text: `
      Компонент 'Сворачиваемый список'
      ~~~js
      import Collapse, { Panel } from 'n2o-framework/lib/components/snippets/Collapse/Collapse';
      
      <Collapse defaultActiveKey="1">
        <Panel key="1" header="Первый" type="line" >{textToPanel}</Panel>
        <Panel key="2" header="Второй" type="line" >{textToPanel}</Panel>
        <Panel key="3" header="Третий" type="line" >{textToPanel}</Panel>
      </Collapse>
      ~~~
      `,
            },
        },
    )
    .add(
        'Компонент type=divider',
        () => <CollapseToStory type="divider" />,
        {
            info: {
                text: `
      Компонент 'Сворачиваемый список'
      ~~~js
      import Collapse, { Panel } from 'n2o-framework/lib/components/snippets/Collapse/Collapse';
      
      <Collapse defaultActiveKey="1">
        <Panel key="1" header="Первый" type="divider" >{textToPanel}</Panel>
        <Panel key="2" header="Второй" type="divider" >{textToPanel}</Panel>
        <Panel key="3" header="Третий" type="divider" >{textToPanel}</Panel>
      </Collapse>
      ~~~
      `,
            },
        },
    )
    .add(
        'Компонент с InputSelect внутри',
        () => (
            <Collapse defaultActiveKey="1">
                <Panel key="1" header="Первый">
                    <InputSelect {...InputSelectJson} options={InputSelectJson.value} />
                </Panel>
                <Panel key="2" header="Второй">
                    <InputSelect {...InputSelectJson} options={InputSelectJson.value} />
                </Panel>
            </Collapse>
        ),
        {
            info: {
                text: `
      Компонент 'Сворачиваемый список'
      ~~~js
      import Collapse, { Panel } from 'n2o-framework/lib/components/snippets/Collapse/Collapse';
      import InputSelect from 'n2o-framework/lib/components/controls/InputSelect/InputSelect';
      
      <Collapse defaultActiveKey="1">
        <Panel key="1" header="Первый">
          <InputSelect {...inputSelectProps} options={InputSelectJson.value} />
        </Panel>
        <Panel key="2" header="Второй">
          <InputSelect {...inputSelectProps} options={InputSelectJson.value} />
        </Panel>
      </Collapse>
      ~~~
      `,
            },
        },
    )
    .add(
        'Компонент с длинным заголовком',
        () => (
            <Collapse style={{ maxWidth: '800px' }} defaultActiveKey="1">
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
        ),
        {
            info: {
                text: `
      Компонент 'Сворачиваемый список'
      ~~~js
      import Collapse, { Panel } from 'n2o-framework/lib/components/snippets/Collapse/Collapse';
      import InputSelect from 'n2o-framework/lib/components/controls/InputSelect/InputSelect';
      
      <Collapse defaultActiveKey="1">
        <Panel
          key="1"
          type="default"
          header={longText}
        >
          {textToPanel}
        </Panel>
        <Panel
          key="2"
          type="line"
          header={longText}
        >
          {textToPanel}
        </Panel>
        <Panel
          key="3"
          type="divider"
          header={longText}
        >
          {textToPanel}
        </Panel>
        <Panel
          key="4"
          type="divider"
          header={longText}
        >
          {textToPanel}
        </Panel>
      </Collapse>
      ~~~
      `,
            },
        },
    )
    .add(
        'Создание через Factory',
        () => {
            const dt = {
                id: 'uniqId',
                src: 'Collapse',
                type: 'default',
                defaultActiveKey: '1',
                destroyInactivePanel: false,
                accordion: false,
                dataKey: 'items',
                items: [
                    { key: '1', header: 'Первый', text: 'Teкст' },
                    { key: '2', header: 'Второй', text: 'Teкст' },
                ],
            }

            return (
                <>
                    <Factory level={SNIPPETS} id="uniqId" {...dt} />
                </>
            )
        },
        {
            info: {
                text: `
      Компонент 'Сворачиваемый список'
      ~~~js
      import Factory from 'n2o-framework/lib/core/factory/Factory';
      
      <Factory level={SNIPPETS} id="uniqId" {...collapseProps} />
      ~~~
      `,
            },
        },
    )
