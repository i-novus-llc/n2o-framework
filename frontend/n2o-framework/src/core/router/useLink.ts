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
    const { url,
        pathMapping,
        queryMapping,
        model,
        visible,
        enabled } = options

    const { getState } = useStore()
    const { url: compiledUrl } = dataProviderResolver(getState(), { url, pathMapping, queryMapping })

    return {
        url: compiledUrl,
        visible: typeof visible === 'string' ? executeExpression<boolean, undefined>(visible, model) : visible,
        enabled: typeof enabled === 'string' ? executeExpression<boolean, undefined>(enabled, model) : enabled,
    }
}
