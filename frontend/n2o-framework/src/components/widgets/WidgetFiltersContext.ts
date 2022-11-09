import { createContext } from 'react'

export interface IWidgetFiltersContext {
    formName: string
    filter(): void
    reset(fetchOnClear: boolean): void
}

export const WidgetFiltersContext = createContext<IWidgetFiltersContext>({
    formName: '',
    filter: () => {},
    reset: () => {},
})
