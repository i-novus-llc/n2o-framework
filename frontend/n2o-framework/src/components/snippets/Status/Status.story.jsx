import React from 'react'
import { storiesOf } from '@storybook/react'

import Status from './Status'

const stories = storiesOf('UI Компоненты/Status', module)

const props = {
    text: 'Status text',
    icon: 'fa fa-edit',
}

stories.add('Компонент', () => <Status {...props} />)

stories.add('Позиционирование иконки', () => (
    <div
        style={{
            display: 'flex',
            justifyContent: 'space-between',
            width: '250px',
        }}
    >
        <Status {...props} />
        <Status {...props} iconDirection="right" />
    </div>
))
