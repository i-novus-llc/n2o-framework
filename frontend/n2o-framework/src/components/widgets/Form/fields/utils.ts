import { FieldDependency } from '../../../../ducks/form/types'
import { replaceIndex as replaceIndexKey } from '../../../../core/datasource/ArrayField/replaceIndex'

const replaceWord = (str: string, from: string, to: string) => {
    // \b и \$ кофликтуют, когда стоят рядом
    const prefix = from.startsWith('$') ? '' : '\\b'
    const suffix = from.endsWith('$') ? '' : '\\b'
    // Экранируем, чтобы регулярка не ломалась на спецсимволе
    const word = from.replace('$', '\\$')
    const reg = new RegExp(`${prefix}${word}${suffix}`, 'g')

    return str.replaceAll(reg, to)
}

export const modifyDependencies = (
    dependencies: FieldDependency[] | void,
    ctx: Record<string, number>,
) => dependencies?.map((dep) => {
    const newDep = { ...dep }

    if (newDep.on) {
        newDep.on = newDep.on.map(key => replaceIndexKey(key, ctx))
    }

    return newDep
})

export const replaceIndex = (
    obj: object,
    ctx: Record<string, number>,
) => {
    let jsonString = JSON.stringify(obj)

    for (const [key, index] of Object.entries(ctx)) {
        jsonString = replaceWord(jsonString, key, `${index}`)
    }

    return JSON.parse(jsonString)
}

export const resolveControlIndexes = (
    control: { dataProvider?: object },
    ctx: Record<string, number>,
) => {
    if (control.dataProvider) {
        const dataProvider = replaceIndex(control.dataProvider, ctx)

        return {
            ...control,
            dataProvider,
        }
    }

    return control
}
