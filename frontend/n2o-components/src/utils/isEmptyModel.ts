import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'

export function isEmptyModel(model: Record<string, unknown> | undefined | object): boolean {
    return !model || isEmpty(model) || Object.values(model).every(value => value === '' || isNil(value))
}
