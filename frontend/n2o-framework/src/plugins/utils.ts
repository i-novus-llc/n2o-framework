import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'
import merge from 'lodash/merge'

import { dataProviderResolver } from '../core/dataProviderResolver'
import propsResolver from '../utils/propsResolver'
import { DataSourceModels } from '../core/datasource/const'

import { Item } from './CommonMenuTypes'

export type metaPropsType = { [key: string]: unknown }

export const getFromSource = (
    itemProps: Item,
    datasources: metaPropsType[],
    models?: DataSourceModels,
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

    if (models && !isEmpty(models.datasource)) {
        // @ts-ignore FIXME разобраться с типами
        return merge(propsResolver(props, initialModel), propsResolver(props, models.datasource))
    }

    if (datasource in datasources) {
        return propsResolver(props, initialModel)
    }

    return props
}
