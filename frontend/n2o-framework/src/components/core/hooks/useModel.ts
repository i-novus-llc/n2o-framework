import { useSelector } from 'react-redux'

import { ModelPrefix } from '../../../core/datasource/const'
import { modelsSelector } from '../../../ducks/models/selectors'

type Model = Record<string, unknown> | Array<Record<string, unknown>>

export const useModel = (datasource?: string, prefix?: ModelPrefix): Model => {
    const models = useSelector(modelsSelector)

    if (datasource && prefix) {
        return (models[prefix]?.[datasource] || {}) as Model
    }

    return {}
}
