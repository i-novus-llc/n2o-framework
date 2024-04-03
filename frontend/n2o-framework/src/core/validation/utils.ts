import { get } from 'lodash'

import { ContextType } from '../datasource/ArrayField/Context'

import { INDEX_REGEXP } from './const'

export const keyToRegexp = (validationKey: string) => new RegExp(`^${validationKey}`.replace(
    INDEX_REGEXP,
    '\\[(\\d+)].',
))

export const isMulti = (validationKey: string) => validationKey.match(INDEX_REGEXP)

export const filterByFields = (validationKey: string, fields: string[]): boolean => {
    if (isMulti(validationKey)) {
        const regexp = keyToRegexp(validationKey)

        return fields.some(field => field.match(regexp))
    }

    return fields.includes(validationKey)
}

/**
 * Получение контекста мультисетов для выполняемых выражений из имени поля
 */
export const getCtxFromField = (
    fieldName: string,
    regexp: RegExp,
) => {
    const match = fieldName.match(regexp)

    if (!match) { return null }

    const [, ...indexList] = match

    const ctx: ContextType = {}

    indexList.forEach((index, depth) => {
        if (depth === 0) {
            ctx.index = +index
        }

        ctx[`$index_${depth}`] = +index
    })

    return ctx
}

type FieldContext = [string, ContextType]
/**
 * Получения массива строк и контекста мультисетов для выполняемых выражений по ключу валидации и  текущей модели
 */
export const getCtxByModel = (validationKey: string, model: object) => {
    // split с регуляркой криво режет, закидывая в реультат часть строки, которую проверяет регулярка
    // поэтому сначала заменяем на что-то более удобное, потом split'уем
    const path = validationKey.replaceAll(INDEX_REGEXP, '[*]').split('[*]')

    function f(
        path: string[],
        model: object,
        ctx: ContextType = {},
        depth = 0,
        fullName = '',
    ): FieldContext[] {
        // если упёрлись в конец - косяк где-то выше, просто возвращаем пусой массив
        if (!path.length) { return [] }
        // послежний элемент в path это ключ конкретного поля, глубже идти не надо, клеим что есть и отдаём
        if (path.length === 1) { return [[`${fullName}${fullName ? '.' : ''}${path[0]}`, ctx]] }

        const [key, ...subPath] = path

        const list: object[] = get(model, key, [])
        let res: FieldContext[] = []

        for (let i = 0; i < list.length; i++) {
            const fieldName = `${key}[${i}]`
            const subModel = list[i]
            const context: ContextType = {
                ...ctx,
                [`$index_${depth}`]: i,
            }

            if (depth === 0) { context.index = i }

            res = [
                ...res,
                ...f(subPath, subModel, context, depth + 1, `${fullName}${fullName ? '.' : ''}${fieldName}`),
            ]
        }

        return res
    }

    return f(path, model)
}
