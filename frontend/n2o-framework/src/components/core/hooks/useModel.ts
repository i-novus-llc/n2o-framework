import { useSelector } from 'react-redux'

import { ModelPrefix } from '../../../core/datasource/const'
import { modelsSelector } from '../../../ducks/models/selectors'

export const useModel = (datasource?: string, prefix?: ModelPrefix) => {
    const models = useSelector(modelsSelector)

    if (datasource && prefix) {
        return models[prefix]?.[datasource] || {}
    }

    return {}
}
