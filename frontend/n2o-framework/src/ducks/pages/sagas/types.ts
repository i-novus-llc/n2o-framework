import { Action } from 'redux'

import { MappingParam } from '../../models/Models'

export interface Location {
    pathname: string
    search: string
}

export type Routes = {
    queryMapping: Record<string, {
        get: Action
        set: MappingParam
    }>
    subRoutes?: string[]
    path?: string
}
