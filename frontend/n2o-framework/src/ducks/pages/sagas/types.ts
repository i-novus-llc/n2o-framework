import { Action } from 'redux'

import { MappingParam } from '../../datasource/Provider'

export interface Location {
    pathname: string
    search: string
}

// В connected-react-router кривой тип экшена
export interface LocationChangeAction {
    type: string
    payload: {
        location: Location
    }
}

export interface Route {
    exact?: boolean
    isOtherPage?: boolean
    path: string
}

// TODO наверно, надо будет куда то в root типы метаданных закинуть
export type Routes = {
    list: Route[]
    queryMapping: Record<string, {
        get: Action
        set: MappingParam
    }>
    pathMapping: Record<string, {
        get: Action
        set: MappingParam
    }>
}
