import React, { useEffect } from 'react'
import { isEqual } from 'lodash'
import { useSelector } from 'react-redux'

import { widgetPropTypes } from '../../../core/widget/propTypes'
import { usePrevious } from '../../../utils/usePrevious'
import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'

/**
 * Обётка для списковых виджетов, отвечающая за проставление активной модели из datasource[0]
 */
export function WithActiveModel(Widget, shouldUpdate = () => true) {
    const WithActiveModel = (props) => {
        const { datasource: datasourceName, setResolve } = props
        const datasourceModel = useSelector(dataSourceModelByPrefixSelector(datasourceName, ModelPrefix.source))
        const resolveModel = useSelector(dataSourceModelByPrefixSelector(datasourceName, ModelPrefix.active))
        const prevSource = usePrevious(datasourceModel)

        useEffect(() => {
            if (
                shouldUpdate(props) &&
                !isEqual(datasourceModel, prevSource) && (
                    !resolveModel ||
                    (resolveModel && !datasourceModel.some(model => isEqual(model, resolveModel)))
                )
            ) {
                setResolve(datasourceModel[0])
            }
        }, [setResolve, datasourceModel, prevSource, resolveModel, props])

        return <Widget {...props} />
    }

    WithActiveModel.propTypes = {
        ...widgetPropTypes,
    }

    return WithActiveModel
}
