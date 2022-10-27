import isEmpty from 'lodash/isEmpty'

import { IDataSourceModels, ModelPrefixes } from '../../core/datasource/const'
import { parseExpression } from '../../utils/evalExpression'
// @ts-ignore ignore import error from js file
import propsResolver from '../../utils/propsResolver'

export const textResolver = (
    models: IDataSourceModels,
    text: string | null,
    modelPrefix?: ModelPrefixes,
): string | null => {
    if (!text) {
        return null
    }

    if (!modelPrefix || !parseExpression(text)) {
        return text
    }

    const model = models[modelPrefix]

    if (isEmpty(model)) {
        return null
    }

    const { text: resolvedText } = propsResolver({ text }, model)

    return resolvedText
}
