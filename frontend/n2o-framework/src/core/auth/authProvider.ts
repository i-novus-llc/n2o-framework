import { ResponseStatus } from '../../constants/ResponseStatus'

import { Security, UserConfig } from './types'
import {
    SECURITY_LOGIN,
    SECURITY_LOGOUT,
    SECURITY_ERROR,
    SECURITY_CHECK,
    SECURITY_INITIALIZE,
} from './authTypes'
import { defaultProvider } from './Provider'

export const checkPermission = defaultProvider.check

export default (
    type: string,
    params: {
        config: Security
        status?: ResponseStatus
        user: UserConfig
    },
) => {
    switch (type) {
        case SECURITY_INITIALIZE: {
            return Promise.resolve(null)
        }
        case SECURITY_LOGIN: {
            return defaultProvider.login(params)
        }
        case SECURITY_LOGOUT: {
            return defaultProvider.logout()
        }
        case SECURITY_ERROR: {
            const { status } = params

            return status === ResponseStatus.Unauthorized
                ? Promise.reject(params)
                : Promise.resolve(params)
        }
        case SECURITY_CHECK: {
            const { config, user } = params

            if (!config) { return Promise.resolve(true) }

            const configList = Array.isArray(config) ? config : [config]
            const hasAccess = configList.every(
                config => Object.values(config).every(
                    cfg => defaultProvider.check(cfg, user),
                ),
            )

            return hasAccess ? Promise.resolve(true) : Promise.reject(new Error('Нет доступа.'))
        }
        default: {
            return Promise.reject(new Error('Неверно задан тип для authProvider!'))
        }
    }
}
