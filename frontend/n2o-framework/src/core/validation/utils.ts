import get from 'lodash/get'

import { ContextType } from '../datasource/ArrayField/Context'

import { INDEX_MASK, INDEX_REGEXP } from './const'

const REPLACER = '[*]'

export const isMulti = (validationKey: string) => validationKey.match(INDEX_REGEXP)

/**
 * Получение контекста мультисетов для выполняемых выражений из имени поля
 */
export const getCtxFromField = (fieldName: string) => {
    const regexp = new RegExp(fieldName.replaceAll(/\[\d+]/g, INDEX_MASK))
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

export const createRegexpWithContext = (
    str: string,
    ctx: ContextType | null,
) => {
    const mask = ctx
        ? str.replaceAll(INDEX_REGEXP, (_, key) => {
            const index = ctx[key as keyof ContextType]

            return typeof index === 'undefined'
                ? REPLACER
                : `\\[${index}]`
        })
        : str

    return new RegExp(`^${mask.replaceAll(REPLACER, INDEX_MASK)}$`)
}

type FieldContext = [string, ContextType]

/**
 * Получения массива строк и контекста мультисетов для выполняемых выражений по ключу валидации и  текущей модели
 */
export const getCtxByModel = (validationKey: string, model: object) => {
    // split с регуляркой криво режет, закидывая в реультат часть строки, которую проверяет регулярка
    // поэтому сначала заменяем на что-то более удобное, потом split'уем
    const path = validationKey.replaceAll(INDEX_REGEXP, REPLACER).split(`${REPLACER}.`)

    function f(
        path: string[],
        model: object,
        ctx: ContextType = {},
        depth = 0,
        fullName = '',
    ): FieldContext[] {
        // если упёрлись в конец - косяк где-то выше, просто возвращаем пусой массив
        if (!path.length) { return [] }
        // последний элемент в path это ключ конкретного поля, глубже идти не надо, клеим что есть и отдаём
        if (path.length === 1) { return [[`${fullName}${fullName ? '.' : ''}${path[0]}`, ctx]] }

        const [key, ...subPath] = path

        const list: object[] = get(model, key) ?? []
        const res: FieldContext[] = []

        if (!Array.isArray(list)) {
            console.warn(
                `Ошибка валидации multi-set: модель "${fullName}${fullName ? '.' : ''}${key}" не является списком`,
                list,
            )

            return res
        }

        for (let i = 0; i < list.length; i++) {
            const fieldName = `${key}[${i}]`
            const subModel = list[i]
            const context: ContextType = {
                ...ctx,
                [`$index_${depth}`]: i,
            }

            if (depth === 0) { context.index = i }

            res.push(
                ...f(subPath, subModel, context, depth + 1, `${fullName}${fullName ? '.' : ''}${fieldName}`),
            )
        }

        return res
    }

    return f(path, model)
}
