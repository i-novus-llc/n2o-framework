import isNil from 'lodash/isNil'

import { TInputNumberValue } from '../InputNumber/types'

export const parseValue = (value: TInputNumberValue, min: number, max: number) => {
    if (value === '-' || value === '' || isNil(value)) {
        return min || 0
    }

    const numberValue = Number(value)

    if (!isNil(min) && numberValue < min) {
        return min
    }
    if (!isNil(max) && numberValue > max) {
        return max
    }

    return numberValue
}
