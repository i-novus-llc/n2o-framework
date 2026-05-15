import { type Csp } from '@i-novus/n2o-components/lib/display/Html'

export interface State {
    id?: string | null
    name?: string | null
    roles?: string[] | null
    isLoggedIn?: boolean
    inProgress?: boolean
    csp?: Csp
}
