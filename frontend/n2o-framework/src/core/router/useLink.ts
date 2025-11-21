import { useStore } from 'react-redux'

import { type Model } from '../../ducks/models/selectors'
import { type Mapping } from '../../ducks/datasource/Provider'
import { executeExpression } from '../Expression/execute'
import { dataProviderResolver } from '../dataProviderResolver'

type Options = {
    url?: string,
    pathMapping?: Mapping
    queryMapping?: Mapping
    model: Model,
    visible: boolean | string
    enabled: boolean | string
}

export function useLink(options: Options) {
    const {
        url,
        pathMapping,
        queryMapping,
        model,
        visible,
        enabled,
    } = options

    const { getState } = useStore()

    let compiledUrl: string | undefined
    let hasUrlError = false

    try {
        const result = dataProviderResolver(getState(), { url, pathMapping, queryMapping })

        compiledUrl = result.url
    } catch (error) {
        compiledUrl = undefined
        hasUrlError = true
    }

    const isVisible = typeof visible === 'string'
        ? executeExpression<boolean, undefined>(visible, model)
        : visible

    let disabled: boolean

    if (hasUrlError) {
        disabled = true
    } else {
        disabled = typeof enabled === 'string'
            ? !executeExpression<boolean, undefined>(enabled, model)
            : !enabled
    }

    return {
        url: compiledUrl,
        visible: isVisible,
        disabled,
    }
}
