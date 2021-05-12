import React from 'react'
import { storiesOf } from '@storybook/react'

import Wireframe from '../../snippets/Wireframe/Wireframe'

import { ListItem } from './ListItem'
import { List } from './List'

const stories = storiesOf('Регионы/Лист', module)

stories.add('Компонент', () => (
    <List>
        <ListItem id="one" title="Один" active>
            <div style={{ width: '100%', height: 100, position: 'relative' }}>
                <Wireframe title="Первый элемент" />
            </div>
        </ListItem>
        <ListItem id="two" title="Два">
            <div style={{ width: '100%', height: 100, position: 'relative' }}>
                <Wireframe title="Второй элемент" />
            </div>
        </ListItem>
        <ListItem id="three" title="Три">
            <div style={{ width: '100%', height: 100, position: 'relative' }}>
                <Wireframe title="Третий элемент" />
            </div>
        </ListItem>
    </List>
))
