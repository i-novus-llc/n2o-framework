import isNil from 'lodash/isNil'
import toNumber from 'lodash/toNumber'
import isNaN from 'lodash/isNaN'
import isNull from 'lodash/isNull'
import isInteger from 'lodash/isInteger'

import { TInputNumberValue } from './types'

export const formatToFloat = (val: TInputNumberValue, stepPrecision?: number) => {
    if (isNil(val) || val === '') { return null }
    const str = Number(val)
        .toString()
        .trim()
        .replace(',', '.')
    const end =
    !str.includes('.', str.indexOf('.') + 1)
        ? str.length
        : str.indexOf('.', str.indexOf('.') + 1)
    const formattedStr = str.slice(0, end)

    return !stepPrecision
        ? toNumber(formattedStr)
        : toNumber(formattedStr).toFixed(stepPrecision)
}

export const getPrecision = (propsStep: number | string, value?: TInputNumberValue) => {
    const needFractionalChar = !isInteger(Number(value)) && isInteger(Number(propsStep))
    const step = needFractionalChar ? `${propsStep}.0` : propsStep

    const stepArr = step
        .toString()
        .trim()
        .split('.')

    return stepArr.length === 1 ? 0 : stepArr[1].length
}

export const isValid = (val: TInputNumberValue, min: number, max: number) => {
    if (!min && !max) {
        return true
    }

    const maxValue = toNumber(max)
    const minValue = toNumber(min)
    const value = toNumber(val)

    if (!isNaN(minValue) && !isNaN(maxValue)) {
        return !isNil(val) && (value <= maxValue && value >= minValue)
    }

    return !isNil(val) && (value <= maxValue || value >= minValue)
}

export const prepareValue = (value: TInputNumberValue) => {
    if (typeof value === 'string') {
        value = value.replace(',', '.')
    }
    if (value === '') {
        return null
    }

    if (value === '-') {
        return value
    }

    return toNumber(value)
}

export function matchesWhiteList(val: TInputNumberValue) {
    if (isNull(val)) {
        return false
    }

    return /^-?\d*[,.]?\d*$/.test(String(val))
}
