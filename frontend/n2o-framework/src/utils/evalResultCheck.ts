import isUndefined from 'lodash/isUndefined'

export const evalResultCheck = (evalResult: string | boolean | void) => !isUndefined(evalResult) && evalResult
