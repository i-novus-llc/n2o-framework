import React from 'react';
import { storiesOf } from '@storybook/react';

import Tabs from './Tabs';
import Tab from './Tab';
import Wireframe from '../../snippets/Wireframe/Wireframe';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Регионы/Вкладки', module);

stories.addParameters({
  info: {
    propTablesExclude: [Wireframe, Factory],
  },
});

stories
  .add(
    'Компонент',
    () => {
      return (
        <Tabs>
          <Tab id="one" title="Один" active>
            <div style={{ width: '100%', height: 400, position: 'relative' }}>
              <Wireframe title="Первый таб" />
            </div>
          </Tab>
          <Tab id="two" title="Два">
            <div style={{ width: '100%', height: 400, position: 'relative' }}>
              <Wireframe title="Второй таб" className="d-10" />
            </div>
          </Tab>
          <Tab id="three" title="Три">
            <div style={{ width: '100%', height: 400, position: 'relative' }}>
              <Wireframe title="Третий таб" className="l-10" />
            </div>
          </Tab>
        </Tabs>
      );
    },
    {
      info: {
        text: `
      Компонент 'Табы'
      ~~~js
      import Tabs, Tab from 'n2o-framework/lib/components/regions/Tabs/Tabs';
      
      <Tabs>
        <Tab id="one" title="Один" active>
          <div style={{ width: '100%', height: 400, position: 'relative' }}>
            <Wireframe title="Первый таб" />
          </div>
        </Tab>
        <Tab id="two" title="Два">
          <div style={{ width: '100%', height: 400, position: 'relative' }}>
            <Wireframe title="Второй таб" className="d-10" />
          </div>
        </Tab>
        <Tab id="three" title="Три">
          <div style={{ width: '100%', height: 400, position: 'relative' }}>
            <Wireframe title="Третий таб" className="l-10" />
          </div>
        </Tab>
      </Tabs>
      ~~~
      `,
      },
    }
  )
  .add(
    'С отключенной вкладкой',
    () => {
      return (
        <Tabs>
          <Tab id="one" title="Один" active>
            <div style={{ width: '100%', height: 400, position: 'relative' }}>
              <Wireframe title="Первый таб" />
            </div>
          </Tab>
          <Tab id="two" title="Два(этот таб отключен)" disabled>
            <div style={{ width: '100%', height: 400, position: 'relative' }}>
              <Wireframe title="Второй таб" />
            </div>
          </Tab>
          <Tab id="three" title="Три">
            <div style={{ width: '100%', height: 400, position: 'relative' }}>
              <Wireframe title="Третий таб" />
            </div>
          </Tab>
        </Tabs>
      );
    },
    {
      info: {
        text: `
      Компонент 'Табы'
      ~~~js
      import Tabs, Tab from 'n2o-framework/lib/components/regions/Tabs/Tabs';
      
      <Tabs>
        <Tab id="one" title="Один" active>
          <div style={{ width: '100%', height: 400, position: 'relative' }}>
            <Wireframe title="Первый таб" />
          </div>
        </Tab>
        <Tab id="two" title="Два(этот таб отключен)" disabled>
          <div style={{ width: '100%', height: 400, position: 'relative' }}>
            <Wireframe title="Второй таб" />
          </div>
        </Tab>
        <Tab id="three" title="Три">
          <div style={{ width: '100%', height: 400, position: 'relative' }}>
            <Wireframe title="Третий таб" />
          </div>
        </Tab>
      </Tabs>
      ~~~
      `,
      },
    }
  )
  .add(
    'С иконками',
    () => {
      return (
        <Tabs>
          <Tab id="one" title="Google" icon="fa fa-google" active>
            <div style={{ width: '100%', height: 400, position: 'relative' }}>
              <Wireframe title="Первый таб" />
            </div>
          </Tab>
          <Tab id="two" title="Facebook" icon="fa fa-facebook">
            <div style={{ width: '100%', height: 400, position: 'relative' }}>
              <Wireframe title="Второй таб" />
            </div>
          </Tab>
          <Tab id="three" title="Amazon" icon="fa fa-amazon">
            <div style={{ width: '100%', height: 400, position: 'relative' }}>
              <Wireframe title="Третий таб" />
            </div>
          </Tab>
        </Tabs>
      );
    },
    {
      info: {
        text: `
      Компонент 'Табы'
      ~~~js
      import Tabs, Tab from 'n2o-framework/lib/components/regions/Tabs/Tabs';
      
      <Tabs>
        <Tab id="one" title="Google" icon="fa fa-google" active>
          <div style={{ width: '100%', height: 400, position: 'relative' }}>
            <Wireframe title="Первый таб" />
          </div>
        </Tab>
        <Tab id="two" title="Facebook" icon="fa fa-facebook">
          <div style={{ width: '100%', height: 400, position: 'relative' }}>
            <Wireframe title="Второй таб" />
          </div>
        </Tab>
        <Tab id="three" title="Amazon" icon="fa fa-amazon">
          <div style={{ width: '100%', height: 400, position: 'relative' }}>
            <Wireframe title="Третий таб" />
          </div>
        </Tab>
      </Tabs>
      ~~~
      `,
      },
    }
  );
