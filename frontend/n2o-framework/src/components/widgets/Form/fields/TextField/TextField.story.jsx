import React from 'react'
import { storiesOf } from '@storybook/react'

import TextField from './TextField'

const stories = storiesOf('Виджеты/Форма/Fields/TextField')

stories
    .add('Текст', () => <TextField text="Обычный текст" />)
    .add('Отформатированный текст', () => (
        <TextField text="20.04.1934 00:00:00" format="date DD.MM.YYYY" />
    ))
