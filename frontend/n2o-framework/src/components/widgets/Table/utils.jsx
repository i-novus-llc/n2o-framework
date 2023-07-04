import findIndex from 'lodash/findIndex'

export const getIndex = (datasource, selectedId) => {
    // eslint-disable-next-line eqeqeq
    const index = findIndex(datasource, model => model.id == selectedId)

    return index >= 0 ? index : 0
}
