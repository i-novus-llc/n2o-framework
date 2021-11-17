import { createContext } from 'react'

/* eslint-disable no-unused-vars */
const METHODS = {
    fetchData() {},
    setFilter(filterModel) {},
    setResolve(model) {},
    setSelected(models) {},
    setSorting(sorting) {},
}

export const WidgetContext = createContext(METHODS)
