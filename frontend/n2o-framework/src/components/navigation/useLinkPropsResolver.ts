import { useStore } from 'react-redux'

import { type Model } from '../../ducks/models/selectors'
import { type Mapping } from '../../ducks/datasource/Provider'
import { executeExpression } from '../../core/Expression/execute'
import { dataProviderResolver } from '../../core/dataProviderResolver'

type Options = {
    url?: string,
    pathMapping?: Mapping
    queryMapping?: Mapping
    model: Model,
    visible: boolean | string
    enabled: boolean | string
}

export function useLinkPropsResolver(options: Options) {
    const {
        url,
        pathMapping,
        queryMapping,
        model,
        visible,
        enabled,
    } = options

    const { getState } = useStore()

    const isVisible = typeof visible === 'string'
        ? executeExpression<boolean, undefined>(visible, model)
        : visible

    try {
        const compiledUrl = url && dataProviderResolver(getState(), { url, pathMapping, queryMapping }).url

        return {
            url: compiledUrl,
            visible: isVisible,
            disabled: typeof enabled === 'string'
                ? !executeExpression<boolean, undefined>(enabled, model)
                : !enabled,
        }
    } catch (error) {
        return {
            url: undefined,
            visible: isVisible,
            disabled: true,
        }
    }
}
