import { useSelector, useStore } from 'react-redux'

import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'
import { type Mapping } from '../../ducks/datasource/Provider'
import { dataProviderResolver } from '../../core/dataProviderResolver'
import { useResolved } from '../../core/Expression/useResolver'
import { ModelPrefix } from '../../core/datasource/const'

type Options = {
    url?: string,
    pathMapping?: Mapping
    queryMapping?: Mapping
    model: ModelPrefix,
    datasource: string,
    enabled?: boolean | string
    visible?: boolean | string
}

type Result = {
    url?: string
    disabled: boolean
    enabled: boolean
    visible: boolean
}

export function useLinkPropsResolver<
    P,
    O extends Options & P,
    R extends Result & Omit<O, keyof Options>,
>({
    url,
    pathMapping,
    queryMapping,
    model: prefix,
    datasource,
    visible: propsVisible = true,
    enabled: propsEnabled = true,
    ...rest
}: O): R {
    const model = useSelector(getModelByPrefixAndNameSelector(prefix, datasource))
    const { getState } = useStore()
    const { enabled, ...resolved } = useResolved<R>({
        visible: propsVisible,
        enabled: propsEnabled,
        ...rest,
    }, model)

    try {
        const compiledUrl = url && dataProviderResolver(getState(), { url, pathMapping, queryMapping }).url

        return {
            ...resolved,
            url: compiledUrl,
            disabled: !enabled,
        } as R
    } catch (error) {
        return {
            ...resolved,
            url: undefined,
            disabled: true,
        } as R
    }
}
