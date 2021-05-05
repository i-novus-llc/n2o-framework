import React from 'react'
import { storiesOf } from '@storybook/react'
import { parseUrl } from 'N2oStorybook/fetchMock'

import { WIDGETS } from '../factory/factoryLevels'
import Factory from '../factory/Factory'

import FieldDependency from './FieldDependency.meta'

const stories = storiesOf('Функциональность/Зависимость между полями', module)

stories.add('Метаданные', () => (
    <Factory
        level={WIDGETS}
        id="Page_Form"
        {...FieldDependency.Page_Form}
    />
))
