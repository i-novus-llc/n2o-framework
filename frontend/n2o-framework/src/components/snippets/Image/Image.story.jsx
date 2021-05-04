import React from 'react'
import { storiesOf } from '@storybook/react'

import Image from './Image'

const stories = storiesOf('UI Компоненты/Изображение', module)

const props = {
    id: 'Image',
    src:
    'https://sf-applications.s3.amazonaws.com/Bear/wallpapers/05/july-2020-wallpaper_desktop-3840x1600.png',
    title: 'Title',
    description: 'description',
    textPosition: 'right',
    visible: true,
}

stories.add('Компонент', () => <Image {...props} />)

stories.add('Позиционирование текста', () => (
    <div>
        <Image {...props} textPosition="top" />
        <Image {...props} textPosition="right" />
        <Image {...props} textPosition="left" />
        <Image {...props} textPosition="bottom" />
    </div>
))

stories.add('Shapes', () => (
    <div>
        <Image {...props} title="rounded" shape="rounded" />
        <Image {...props} title="circle" shape="circle" />
        <Image {...props} title="square" shape="square" />
    </div>
))

stories.add('Кастомная ширина', () => (
    <div>
        <Image
            {...props}
            title="Custom width 150px"
            description="rounded"
            width="150px"
        />
        <Image
            {...props}
            title="Custom width 150px"
            description="circle"
            shape="circle"
            width="150px"
        />
        <Image
            {...props}
            title="Custom width 150px"
            description="square"
            shape="square"
            width="150px"
        />
    </div>
))
