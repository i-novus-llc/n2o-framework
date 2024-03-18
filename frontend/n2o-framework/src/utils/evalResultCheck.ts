import isUndefined from 'lodash/isUndefined'

export const evalResultCheck = (evalResult: unknown) => !isUndefined(evalResult) && Boolean(evalResult)
