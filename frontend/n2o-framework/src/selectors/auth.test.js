import { authSelector, isLoggedInSelector, userSelector } from './auth'

const state = {
    user: {
        name: 'user',
        isLoggedIn: true,
    },
}

describe('Проверка селекторов auth', () => {
    it('authSelector должен вернуть пользователя', () => {
        expect(authSelector(state)).toEqual(state.user)
    })
    it('isloggedInSelector должен вернуть isLoggedIn', () => {
        expect(isLoggedInSelector(state)).toEqual(state.user.isLoggedIn)
    })
    it('userSelector должен вернуть user без isLoggedIn', () => {
        expect(userSelector(state)).toEqual({
            name: state.user.name,
        })
    })
})
