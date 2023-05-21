import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'
import merge from 'lodash/merge'

// @ts-ignore ignore import error from js file
import { dataProviderResolver } from '../core/dataProviderResolver'
// @ts-ignore ignore import error from js file
import { resolveItem } from '../utils/propsResolver'
import { IDataSourceModels } from '../core/datasource/const'
import { IBadgeProps } from '../components/snippets/Badge/Badge'

export type metaPropsType = {[key: string]: unknown}

export interface IItem {
    target: string
    href: string
    linkType: 'outer' | 'inner'
    imageSrc?: string
    icon?: string
    title: string
    imageShape?: string
    badge?: IBadgeProps
    items?: IItem[]
    pathMapping?: metaPropsType
    queryMapping?: metaPropsType
}

export const getFromSource = (
    itemProps: IItem,
    datasources: metaPropsType[],
    datasource: string,
    models: IDataSourceModels,
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
        return merge(resolveItem(props, initialModel), resolveItem(props, models.datasource))
    }

    if (datasource in datasources) {
        return resolveItem(props, initialModel)
    }

    return props
}
