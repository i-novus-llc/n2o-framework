import {
    USER_CHECK,
    USER_CHECK_SUCCESS,
    USER_CHECK_ERROR,
    USER_LOGIN,
    USER_LOGIN_SUCCESS,
    USER_LOGIN_ERROR,
    USER_LOGOUT,
    USER_LOGOUT_SUCCESS,
    USER_LOGOUT_ERROR,
} from '../constants/auth'

const defaultPayload = {
    id: null,
    name: null,
    roles: [],
    isLoggedIn: false,
    inProgress: false,
}

export default (state = defaultPayload, { type, payload, meta }) => {
    switch (type) {
        case USER_LOGIN_SUCCESS:
            return { ...payload, isLoggedIn: true }
        case USER_LOGOUT_SUCCESS:
            return { ...defaultPayload }
        default:
            return state
    }
}
