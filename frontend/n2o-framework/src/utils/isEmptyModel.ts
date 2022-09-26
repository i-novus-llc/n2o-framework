import isEmpty from 'lodash/isEmpty'
import every from 'lodash/every'

export function isEmptyModel(model: Record<string, unknown>): boolean {
    return isEmpty(model) || every(model, value => value === '')
}
