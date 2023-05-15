import React from 'react'
import { compose, mapProps } from 'recompose'
import PropTypes from 'prop-types'
import { useSelector } from 'react-redux'

import { withWidgetHandlers } from '../AdvancedTable/AdvancedTableContainer'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

import { Cards } from './Cards'

const CardsContainer = (props) => {
    const { datasource } = props
    const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasource, ModelPrefix.source))

    return <Cards {...props} data={datasourceModel} />
}

export default compose(
    withWidgetHandlers,
    mapProps(
        ({
            className,
            id,
            cards,
            setResolve,
            dispatch,
            align,
            height,
            datasource,
        }) => ({
            className,
            id,
            cards,
            onResolve: setResolve,
            dispatch,
            align,
            height,
            datasource,
        }),
    ),
)(CardsContainer)

CardsContainer.propTypes = {
    datasource: PropTypes.string,
}
