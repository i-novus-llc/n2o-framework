import React from 'react'
import { compose, mapProps } from 'recompose'

import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer'

import { Cards } from './Cards'

const CardsContainer = props => <Cards {...props} />

export default compose(
    withWidgetHandlers,
    mapProps(
        ({
            models,
            className,
            id,
            cards,
            setResolve,
            dispatch,
            align,
            height,
        }) => ({
            className,
            id,
            cards,
            data: models.datasource,
            onResolve: setResolve,
            dispatch,
            align,
            height,
        }),
    ),
)(CardsContainer)
