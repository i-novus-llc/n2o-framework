import React from 'react'
import { storiesOf, forceReRender } from '@storybook/react'
import { StateDecorator, Store } from '@sambego/storybook-state'

import Template from '../OLD_SidebarFixTemplate'
import Wireframe from '../../components/snippets/Wireframe/Wireframe'

import SideBar, { SideBar as SidebarComponent } from './SideBar'
import sidebarMetadata from './sidebarMetadata.meta.json'

const store = new Store({
    visible: true,
})

store.subscribe(forceReRender)

const stories = storiesOf('UI Компоненты/Меню слева', module)

stories.addDecorator(StateDecorator(store))
stories.addParameters({
    info: {
        propTables: [SidebarComponent],
        propTablesExclude: [SideBar, Wireframe],
    },
})

stories.add(
    'Сжатие',
    () => (
        <>
            <button
                style={{ marginBottom: '10px' }}
                className="btn btn-secondary"
                onClick={() => store.set({ visible: !store.state.visible })}
            >
                <i className="fa fa-bars" />
            </button>
            <Template>
                <SideBar
                    brandImage="https://avatars0.githubusercontent.com/u/25926683?s=200&v=4"
                    activeId="link"
                    items={sidebarMetadata.items}
                    visible={store.get('visible')}
                    collapse={false}
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
        </>
    ),
    {
        info: {
            text: `
      Компонент 'Боковое меню'
      ~~~js
      import Sidebar from 'n2o-framework/lib/plugins/Sidebar/Sidebar';
      
       <SideBar
          brandImage="https://avatars0.githubusercontent.com/u/25926683?s=200&v=4"
          activeId={'link'}
          items={sidebarMetadata.items}
          visible={visible}
          collapse={false}
          color="inverse"
        />
      ~~~
    `,
        },
    },
)
