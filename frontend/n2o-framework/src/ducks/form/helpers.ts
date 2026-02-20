import isEqual from 'lodash/isEqual'

import { State } from '../State'
import { Validation, ValidationsKey } from '../../core/validation/types'
import { dataSourceValidationSelector } from '../datasource/selectors'

type FieldId = string
type ValidationKey = string

export const getValidationFields = (state: State, id: string) => {
    const filterValidation = dataSourceValidationSelector(id, ValidationsKey.FilterValidations)(state) || {}
    const validation = dataSourceValidationSelector(id, ValidationsKey.Validations)(state) || {}

    return Object.keys(filterValidation).length ? filterValidation : validation
}

export function diffKeys <
    TValue extends Record<string, unknown> | undefined | null,
>(first: TValue, second: TValue) {
    if (!first || !second) {
        if (first) { return Object.keys(first) }
        if (second) { return Object.keys(second) }

        return []
    }

    return [...new Set([
        ...Object.keys(first),
        ...Object.keys(second),
    ])].filter(key => !isEqual(first[key], second[key]))
}

export const getDependentSet = (validations: Validation[], actionField: string) => new Set(validations.filter(
    validation => validation.on?.some(mask => actionField.match(mask)),
))

export function pickValidations(
    validations: Record<ValidationKey, Validation[]>,
    fieldName: FieldId,
) {
    if (validations[fieldName]?.length) { return new Set(validations[fieldName]) }

    const mask = new RegExp(`^${fieldName.replaceAll(/\[\d+]/g, '\\[(index|\\$index_\\d+)]')}$`)

    for (const [validationKey, list] of Object.entries(validations || {})) {
        if (mask.test(validationKey)) { return new Set(list) }
    }

    return null
}

export const mapFields = (fields: Record<FieldId, Set<Validation> | null> = {}) => {
    return Object.fromEntries(Object.entries(fields)
        .filter((field): field is [FieldId, Set<Validation>] => !!field[1]?.size)
        .map(field => ([
            field[0],
            [...field[1]],
        ])))
}

export const unionSets = <T, S extends Set<T>>(first: S, second?: S | null) => {
    if (second) {
        // FIXME разобраться почему сборка падает на first.union
        return new Set([
            ...first,
            ...second,
        ])
    }

    return first
}
