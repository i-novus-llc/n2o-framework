import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'
import merge from 'lodash/merge'
import React, { ComponentType } from 'react'

import { dataProviderResolver } from '../core/dataProviderResolver'
import propsResolver from '../utils/propsResolver'
import { Model } from '../ducks/models/selectors'

import { Item } from './CommonMenuTypes'

export type metaPropsType = { [key: string]: unknown }

// TODO рефакторинг
export const getFromSource = (
    itemProps: Item,
    datasources: metaPropsType[],
    model?: Model,
    datasource?: string,
) => {
    const props = { ...itemProps }
    const { href, pathMapping = {}, queryMapping = {} } = props

    if (!isEmpty(pathMapping) || !isEmpty(queryMapping)) {
        const { url } = dataProviderResolver(props, { url: href, pathMapping, queryMapping })

        props.href = url
    }

    if (!datasource) {
        return props
    }

    const defaultFromDataSource = get(datasources, `${datasource}.values`, [])
    const initialModel = defaultFromDataSource
        .reduce((acc: metaPropsType, value: metaPropsType) => ({ ...acc, ...value }), {})

    if (model) {
        return merge(propsResolver(props, initialModel), propsResolver(props, model))
    }

    if (datasource in datasources) {
        return propsResolver(props, initialModel)
    }

    return props
}

export const WithComponentId = (defaultId: string) => {
    return <P extends object>(Component: ComponentType<P>) => {
        return (props: P & { id?: string }) => {
            return <Component {...props} id={props.id || defaultId} />
        }
    }
}
