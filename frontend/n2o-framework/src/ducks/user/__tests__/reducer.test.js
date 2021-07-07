import auth, { userLoginSuccess, userLogoutSuccess } from '../store'

const initialState = {
    id: null,
    name: null,
    roles: [],
    isLoggedIn: false,
    inProgress: false,
}

describe('Тесты auth reducer', () => {
    it('Должен вернуть данные пользователя и залогинить его', () => {
        expect(
            auth(null, {
                type: userLoginSuccess,
                payload: {
                    id: 'userId',
                    name: 'username',
                    roles: [],
                    isLoggedIn: false,
                    inProgress: false,
                },
            }),
        ).toEqual({
            id: 'userId',
            name: 'username',
            roles: [],
            isLoggedIn: true,
            inProgress: false,
        })
    })

    it('Должен вернуть initialState при logout', () => {
        expect(
            auth(null, {
                type: userLogoutSuccess,
            }),
        ).toEqual(initialState)
    })
})
