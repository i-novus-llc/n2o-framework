import some from 'lodash/some'
import has from 'lodash/has'
import isNil from 'lodash/isNil'
import get from 'lodash/get'

export const modelHasRange = modelValues => some(
    modelValues,
    modelValue => has(modelValue, 'begin') && has(modelValue, 'end'),
)

export const isValidRangeModel = modelValues => some(modelValues, (modelValue) => {
    const begin = get(modelValue, 'begin')
    const end = get(modelValue, 'end')

    return !isNil(begin) && !isNil(end)
})

export const isRequiredRangeModel = (modelValues, modelId) => some(modelValues, modelValue => get(modelValue, 'id') === modelId)
