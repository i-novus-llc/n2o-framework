import isNil from 'lodash/isNil'
import toNumber from 'lodash/toNumber'
import isNaN from 'lodash/isNaN'

export function formatToFloat(val, stepPrecision) {
    if (isNil(val) || val === '') { return null }
    const str = val
        .toString()
        .trim()
        .replace(',', '.')
    const end =
    str.indexOf('.', str.indexOf('.') + 1) === -1
        ? str.length
        : str.indexOf('.', str.indexOf('.') + 1)
    const formattedStr = str.slice(0, end)

    return !stepPrecision
        ? toNumber(formattedStr)
        : toNumber(formattedStr).toFixed(stepPrecision)
}

export function getPrecision(step) {
    const stepArr = step
        .toString()
        .trim()
        .split('.')

    return stepArr.length === 1 ? 0 : stepArr[1].length
}

export function isValid(val, min, max) {
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

export function matchesWhiteList(val) {
    return /^-?\d*[,.]?\d*$/.test(val)
}
