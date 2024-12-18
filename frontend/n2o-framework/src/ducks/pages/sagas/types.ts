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

export type Routes = {
    queryMapping: Record<string, {
        get: Action
        set: MappingParam
    }>
    subRoutes?: string[]
    path?: string
}
