import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'
import every from 'lodash/every'

export function isEmptyModel(model) {
    return isEmpty(model) || every(model, value => value === '' || isNil(value))
}
