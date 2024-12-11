import findIndex from 'lodash/findIndex'

export const getIndex = (datasource: Array<{ id: string }>, selectedId: string) => {
    const index = findIndex(datasource, model => String(model.id) === String(selectedId))

    return index >= 0 ? index : 0
}
