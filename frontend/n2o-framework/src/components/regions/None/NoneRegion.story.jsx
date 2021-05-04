import React from 'react'
import { storiesOf } from '@storybook/react'

import { metadataSuccess } from '../../../actions/pages'
import HtmlWidgetJson from '../../widgets/Html/HtmlWidget.meta'
import { makeStore } from '../../../../.storybook/decorators/utils'

import NoneRegionsJson from './NoneRegion.meta.json'
import NoneRegion, { NoneRegion as NoneRegionComponent } from './NoneRegion'

const stories = storiesOf('Регионы/Простой', module)

stories.addParameters({
    info: {
        propTables: [NoneRegionComponent],
        propTablesExclude: [NoneRegion],
    },
})
const { store } = makeStore()

stories.add(
    'Метаданные',
    () => <NoneRegion {...NoneRegionsJson} pageId="Page" />,
    {
        info: {
            text: `
    Компонент 'Простой регион'
    ~~~js
    import NoneRegion from 'n2o-framework/lib/components/regions/None/NoneRegion';
    
    <NoneRegion 
      pageId="Page" 
      items={[
        {
            widgetId: "Page_Html",
            label: "HTML
        }
      ]} 
    />
    ~~~
    `,
        },
    },
)
