import React, { useEffect, ComponentType } from 'react'
import isEqual from 'lodash/isEqual'
import { useSelector } from 'react-redux'

import { usePrevious } from '../../../utils/usePrevious'
import { getModelByPrefixAndNameSelector, Model } from '../../../ducks/models/selectors'
import { ModelPrefix } from '../../../core/models/types'

export interface WidgetProps {
    datasource: string
    setResolve(model: Model): void
}

/**
 * Обётка для списковых виджетов, отвечающая за проставление активной модели из datasource[0]
 */

export function WithActiveModel<Props extends WidgetProps>(
    Widget: ComponentType<Props>,
    shouldUpdate?: (props: Props) => boolean,
) {
    function WithActiveModel(props: Props) {
        const { datasource, setResolve } = props

        const datasourceModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.source, datasource))
        const prevDatasourceModel = usePrevious(datasourceModel)
        const resolveModel = useSelector(getModelByPrefixAndNameSelector(ModelPrefix.active, datasource))

        useEffect(() => {
            const shouldUpdateProps = shouldUpdate ? shouldUpdate(props) : true
            const needToResolve = !resolveModel || !datasourceModel.some(model => isEqual(model, resolveModel))

            if (shouldUpdateProps && !isEqual(datasourceModel, prevDatasourceModel) && needToResolve) {
                setResolve(datasourceModel[0])
            }
        }, [setResolve, datasourceModel, prevDatasourceModel, resolveModel, props])

        return <Widget {...props} />
    }

    return WithActiveModel
}
