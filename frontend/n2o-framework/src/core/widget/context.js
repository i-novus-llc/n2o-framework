import { createContext } from 'react'

/* eslint-disable no-unused-vars */
const METHODS = {
    fetchData() {},
    setFilter(filterModel) {},
    setResolve(model) {},
    setSelected(models) {},
    setSorting(sorting) {},
    setPage(page) {},
    setSize(size) {},
}

export const WidgetContext = createContext(METHODS)
