import isUndefined from 'lodash/isUndefined'

export const evalResultCheck = evalResult => !isUndefined(evalResult) && evalResult
