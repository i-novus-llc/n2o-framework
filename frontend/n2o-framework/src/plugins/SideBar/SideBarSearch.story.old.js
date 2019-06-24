import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';

import SideBar from './SideBar';
import Template from '../OLD_SidebarFixTemplate';
import Wireframe from '../../components/snippets/Wireframe/Wireframe';
import sidebarMetadata from './sidebarMetadata.meta.json';

const stories = storiesOf('UI Компоненты/Меню слева', module);

stories.addDecorator(withKnobs);

stories.addWithJSX('Поиск', () => {
  return (
    <Template>
      <SideBar
        brandImage={
          'https://avatars0.githubusercontent.com/u/25926683?s=200&v=4'
        }
        activeId={'link'}
        search={true}
        items={sidebarMetadata.items}
        color="inverse"
      />
      <div
        style={{
          width: '100%',
          position: 'relative',
        }}
      >
        <Wireframe
          style={{ top: 0, bottom: 0 }}
          className="n2o"
          title="Тело страницы"
        />
      </div>
    </Template>
  );
});
