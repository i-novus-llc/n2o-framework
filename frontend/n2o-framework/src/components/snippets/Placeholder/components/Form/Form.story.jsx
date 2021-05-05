import React from 'react'
import { storiesOf } from '@storybook/react'

import Placeholder from '../../Placeholder'

import meta from './Form.meta'
import Form from './Form'

const stories = storiesOf('UI Компоненты/Placeholder/type=form', module)

stories.addParameters({
    info: {
        propTables: [Form],
    },
})

stories.add(
    'Компонент',
    () => {
        const props = {
            loading: true,
            type: meta.type,
            rows: meta.rows,
            cols: meta.cols,
        }

        return <Placeholder {...props} />
    },
    {
        info: {
            text: `
    Компонент 'Placeholder' формы
    ~~~js
    import Placeholder from 'n2o-framework/lib/components/snippets/Placeholder/Placeholder';
    
    <Placeholder
        loading={true}
        type="form"
        rows={1}
        cols={2}
     />
    ~~~
    `,
        },
    },
)
