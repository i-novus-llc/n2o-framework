import React from 'react'
import { storiesOf } from '@storybook/react'

import Placeholder from '../../Placeholder'

import meta from './Tree.meta'
import Tree from './Tree'

const stories = storiesOf('UI Компоненты/Placeholder/type=tree', module)

stories.addParameters({
    info: {
        propTables: [Tree],
    },
})

stories
    .add(
        'Компонент',
        () => {
            const props = {
                loading: true,
                type: meta.type,
                rows: meta.rows,
                chevron: meta.chevron,
            }

            return <Placeholder {...props} />
        },
        {
            info: {
                text: `
    Компонент 'Placeholder' дерева
    ~~~js
    import Placeholder from 'n2o-framework/lib/components/snippets/Placeholder/Placeholder';
    
    <Placeholder
        loading={true}
        type="tree"
        rows={1}
     />
    ~~~
    `,
            },
        },
    )
    .add(
        'chevron',
        () => (
            <Placeholder chevron rows={10} type="tree" loading />
        ),
        {
            info: {
                text: `
    Компонент 'Placeholder' дерева
    ~~~js
    import Placeholder from 'n2o-framework/lib/components/snippets/Placeholder/Placeholder';
    
    <Placeholder
        loading={true}
        type="tree"
        rows={1}
        chevron={true}
     />
    ~~~
    `,
            },
        },
    )
