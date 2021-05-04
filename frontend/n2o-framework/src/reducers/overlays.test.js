import { INSERT_MODAL, DESTROY, HIDE, SHOW } from '../constants/overlays'

import overlays from './overlays'

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
                    type: INSERT_MODAL,
                    payload: {
                        visible: true,
                        name: 'testName',
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
                    type: SHOW,
                    payload: {
                        name: 'stateOverlay',
                    },
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
                    type: HIDE,
                    payload: {
                        name: 'stateOverlay',
                    },
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
                    type: DESTROY,
                },
            ),
        ).toEqual([])
    })
})
