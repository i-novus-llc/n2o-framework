import { Action } from 'redux'

import { IMappingParam } from '../../datasource/Provider'

export interface Location {
    pathname: string
    search: string
}

// В connected-react-router кривой тип экшена
export interface ILocationChangeAction {
    type: string
    payload: {
        location: Location
    }
}

export interface IRoute {
    exact?: boolean
    isOtherPage?: boolean
    path: string
}

// TODO наверно, надо будет куда то в root типы метаданных закинуть
export interface IRoutes {
    list: IRoute[]
    queryMapping: Record<string, {
        get: Action
        set: IMappingParam
    }>
    pathMapping: Record<string, {
        get: Action
        set: IMappingParam
    }>
}
