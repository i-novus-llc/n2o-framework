import overlays, {
    insertOverlay,
    destroyOverlay,
    hideOverlay,
    showOverlay
} from '../store'

describe('Тесты overlays reducer', () => {
    it('Проверка INSERT', () => {
        expect(
            overlays(
                [
                    {
                        name: 'stateOverlay',
                    },
                ],
                {
                    type: insertOverlay,
                    payload: {
                        visible: true,
                        name: 'testName',
                        mode: 'modal'
                    },
                },
            ),
        ).toEqual([
            {
                name: 'stateOverlay',
            },
            {
                mode: 'modal',
                name: 'testName',
                props: {
                    name: 'testName',
                    visible: true,
                },
                visible: true,
            },
        ])
    })

    it('Проверка SHOW', () => {
        expect(
            overlays(
                [
                    {
                        name: 'stateOverlay',
                        modal: {},
                        visible: false,
                    },
                ],
                {
                    type: showOverlay,
                    payload: 'stateOverlay',
                },
            ),
        ).toEqual([
            {
                name: 'stateOverlay',
                modal: {},
                visible: true,
            },
        ])
    })

    it('Проверка HIDE', () => {
        expect(
            overlays(
                [
                    {
                        name: 'stateOverlay',
                        modal: {},
                        visible: true,
                    },
                ],
                {
                    type: hideOverlay,
                    payload: 'stateOverlay',
                },
            ),
        ).toEqual([
            {
                name: 'stateOverlay',
                modal: {},
                visible: false,
            },
        ])
    })

    it('Проверка DESTROY', () => {
        expect(
            overlays(
                [
                    {
                        modal: {
                            name: 'stateOverlay',
                        },
                        visible: true,
                    },
                ],
                {
                    type: destroyOverlay,
                },
            ),
        ).toEqual([])
    })
})
