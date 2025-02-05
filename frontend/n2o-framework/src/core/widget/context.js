import { createContext, useContext } from 'react'

/* eslint-disable no-unused-vars,  @typescript-eslint/no-unused-vars */
export const METHODS = {
    fetchData(options) {},
    setFilter(filterModel) {},
    setResolve(model) {},
    setEdit(model) {},
    setSelected(models) {},
    setSorting(field, sorting) {},
    setPage(page, options) {},
    setSize(size) {},
}

export const DataSourceContext = createContext(METHODS)

export const useDataSourceMethodsContext = () => (
    useContext(DataSourceContext)
)
