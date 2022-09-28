import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'
import every from 'lodash/every'

export function isEmptyModel(model: Record<string, unknown> | undefined): boolean {
    return isEmpty(model) || every(model, value => value === '' || isNil(value))
}
