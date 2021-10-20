import { isEmpty } from 'lodash'

import {
    SECURITY_LOGIN,
    SECURITY_LOGOUT,
    SECURITY_ERROR,
    SECURITY_CHECK,
} from '../../../../core/auth/authTypes'

export default (type, params) => {
    switch (type) {
        case SECURITY_LOGIN:
            return Promise.resolve(params)
        case SECURITY_LOGOUT:
            return Promise.resolve()
        case SECURITY_ERROR:
            return Promise.reject()
        case SECURITY_CHECK:
            if (!isEmpty(params.user && params.user.roles) && !isEmpty(params.config)) {
                if (params.user.username === 'admin') {
                    return Promise.resolve(params.user.roles)
                }

                return Promise.reject()
            } if (!isEmpty(params.user && params.user.roles)) {
                return Promise.resolve(params.user.roles)
            }

            return Promise.reject()

        default:
            // eslint-disable-next-line prefer-promise-reject-errors
            return Promise.reject('WTF!?')
    }
}
