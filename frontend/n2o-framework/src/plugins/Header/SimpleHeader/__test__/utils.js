import { createMemoryHistory as createHistory } from 'history'

import configureStore from '../../../../store'
import factoryPoints from '../../../../core/factory/factoryPoints'

import authProviderExample from './authProviderExample'

const securityConfig = {
    authProvider: authProviderExample,
    redirectPath: '/login',
}
const alertsConfig = {
    timeout: {
        error: 0,
        success: 5000,
    },
}

export const history = createHistory()
const config = {
    security: securityConfig,
    messages: alertsConfig,
    customSagas: [],
    factories: factoryPoints,
}

export const store = configureStore({}, history, config)

export const makeStore = () => ({ store, history, securityConfig })
