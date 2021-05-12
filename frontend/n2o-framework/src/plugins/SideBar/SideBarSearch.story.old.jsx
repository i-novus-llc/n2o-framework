import React from 'react'
import { storiesOf } from '@storybook/react'

import Template from '../OLD_SidebarFixTemplate'
import Wireframe from '../../components/snippets/Wireframe/Wireframe'

// eslint-disable-next-line import/no-named-as-default
import SideBar from './SideBar'
import sidebarMetadata from './sidebarMetadata.meta.json'

const stories = storiesOf('UI Компоненты/Меню слева', module)

stories.add('Поиск', () => (
    <Template>
        <SideBar
            brandImage="https://avatars0.githubusercontent.com/u/25926683?s=200&v=4"
            activeId="link"
            search
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
))
