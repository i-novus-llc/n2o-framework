import intersection from 'lodash/intersection'
import isEmpty from 'lodash/isEmpty'

import { SecurityConfig, UserConfig } from './types'

export interface AuthProvider<
    TLogin extends object = object,
> {
    init(user: UserConfig): UserConfig | Promise<UserConfig>
    login(config: TLogin): UserConfig | Promise<UserConfig>
    logout(): UserConfig | Promise<UserConfig>
    check(config: SecurityConfig, user: UserConfig): boolean | Promise<boolean>
}

export const defaultProvider: AuthProvider<{ user: UserConfig }> = {
    init(user) { return user },

    login(config) { return config.user },

    logout() { return {} },

    check(config, user) {
        if (config.denied) { return false }
        if (config.permitAll) { return true }
        if (config.anonymous) { return !user || isEmpty(user.username) }
        if (!isEmpty(config.permissions) && isEmpty(user.permissions)) { return false }
        if (!isEmpty(user.username)) {
            if (config.authenticated) { return true }

            return !isEmpty(intersection(config.roles, user.roles)) ||
                !isEmpty(intersection(config.permissions, user.permissions)) ||
                !isEmpty(intersection(config.usernames, [user.username]))
        }

        return false
    },
}
