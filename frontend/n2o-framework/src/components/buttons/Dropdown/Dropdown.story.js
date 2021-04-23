import React from 'react'
import { storiesOf } from '@storybook/react'
import { omit } from 'lodash'

import Dropdown from './Dropdown'
import MetaJson from './Dropdown.meta.json'

const stories = storiesOf('Кнопки', module)

stories.add('Выпадающий список', () => (
    <Dropdown {...omit(MetaJson, ['conditions'])} />
))
