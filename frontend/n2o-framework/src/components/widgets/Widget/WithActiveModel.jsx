import React, { useEffect } from 'react'
import { isEqual } from 'lodash'

import { widgetPropTypes } from '../../../core/widget/propTypes'
import { usePrevious } from '../../../utils/usePrevious'

/**
 * Обётка для списковых виджетов, отвечающая за проставление активной модели из datasource[0]
 */
export function WithActiveModel(Widget, shoudUpdate = () => true) {
    const WithActiveModel = (props) => {
        const { models, setResolve } = props
        const { datasource, resolve } = models
        const prevSource = usePrevious(datasource)

        useEffect(() => {
            if (
                shoudUpdate(props) &&
                !isEqual(datasource, prevSource) && (
                    !resolve ||
                    (resolve && !datasource.some(model => isEqual(model, resolve)))
                )
            ) {
                setResolve(datasource[0])
            }
        }, [setResolve, datasource, prevSource, resolve, props])

        return <Widget {...props} />
    }

    WithActiveModel.propTypes = {
        ...widgetPropTypes,
    }

    return WithActiveModel
}
