import { overlaysSelector } from '../selectors'

const state = {
    overlays: [
        {
            some: 'overlay',
        },
    ],
}

describe('Проверка селекторов overlays', () => {
    it('overlaysSelector должен вернуть overlay', () => {
        expect(overlaysSelector(state)).toEqual(state.overlays)
    })
})
