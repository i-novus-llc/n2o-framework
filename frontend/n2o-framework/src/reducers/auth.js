import {
    USER_LOGIN_SUCCESS,
    USER_LOGOUT_SUCCESS,
} from '../constants/auth'

const defaultPayload = {
    id: null,
    name: null,
    roles: [],
    isLoggedIn: false,
    inProgress: false,
}

export default (state = defaultPayload, { type, payload }) => {
    switch (type) {
        case USER_LOGIN_SUCCESS: {
            return { ...payload, isLoggedIn: true }
        }
        case USER_LOGOUT_SUCCESS: {
            return { ...defaultPayload }
        }
        default: {
            return state
        }
    }
}
