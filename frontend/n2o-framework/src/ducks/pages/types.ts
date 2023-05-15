import { ModelPrefix } from '../../core/datasource/const'

import { IRoutes } from './sagas/types'

export type TPageState = Record<string, TPageItem>

// TODO: Разбить на более мелкие типы, если есть возможность собрать из из типов других редюсеров
export type TPageItem = {
    metadata: {
        src: string
        id: string
        routes: IRoutes
        models: object // TODO: Дописать тип
        widget?: TPageWidget
        page?: {
            title: string
            datasource: string
            model: string
            htmlTitle: string
        }
        toolbar?: object
    }
    loading: boolean
    error: boolean
    disabled: boolean
    status: number | null
    spinner: boolean
}

export type TPageWidget = {
    src: string
    id: string
    name: string
    datasource: string
    fetchOnInit: boolean
    toolbar: object
    form: {
        fieldsets: unknown[]
        modelPrefix: `${ModelPrefix}`
        prompt: boolean
        autoFocus: boolean
    }
}
