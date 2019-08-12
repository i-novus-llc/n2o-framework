import React from 'react';
import { setDisplayName } from 'recompose';
import { storiesOf } from '@storybook/react';
import SideBar from './SideBar';
import { SideBar as SidebarComponent } from './SideBar';
import SidebarContainer from './SidebarContainer';
import AuthButtonContainer from '../../core/auth/AuthLogin';
import Template from '../OLD_SidebarFixTemplate';
import Wireframe from '../../components/snippets/Wireframe/Wireframe';
import sidebarMetadata from './sidebarMetadata.meta.json';

const stories = storiesOf('UI Компоненты/Меню слева', module);

const NamedSidebar = setDisplayName('Sidebar')(SideBar);

stories.addParameters({
  info: {
    propTables: [SidebarComponent],
    propTablesExclude: [SideBar, Wireframe, AuthButtonContainer],
  },
});

stories
  .add('Компонент', () => {
    return (
      <Template>
        <SideBar {...sidebarMetadata} />
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
  })
  .add('Ограничение доступа', () => {
    return (
      <Template>
        <SidebarContainer {...sidebarMetadata} />
        <div style={{ width: '100%', position: 'relative' }}>
          <small>
            Введите <mark>admin</mark>, чтобы увидеть скрытый элемент меню
          </small>
          <AuthButtonContainer />
          <br />
        </div>
      </Template>
    );
  });
