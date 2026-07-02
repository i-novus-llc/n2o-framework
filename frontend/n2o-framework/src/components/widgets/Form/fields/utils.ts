import { FieldDependency } from '../../../../ducks/form/types'
import { replaceIndexKey } from '../../../../core/datasource/ArrayField/replaceIndex'

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

export const resolveControlIndexes = (
    control: { dataProvider?: object },
    ctx: Record<string, number>,
) => {
    if (control.dataProvider) {
        const dataProvider = replaceIndexKey(control.dataProvider, ctx)

        return {
            ...control,
            dataProvider,
        }
    }

    return control
}
