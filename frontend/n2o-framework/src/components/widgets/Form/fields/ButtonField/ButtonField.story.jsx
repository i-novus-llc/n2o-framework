import React from 'react'
import { storiesOf } from '@storybook/react'

import ButtonField from './ButtonField'

const stories = storiesOf('Виджеты/Форма/Fields/ButtonField')

stories.add('Компонент', () => (
    <ButtonField
        label='Поле "Кнопка"'
        action={{
            type: 'n2o/button/Dummy',
        }}
    />
))
