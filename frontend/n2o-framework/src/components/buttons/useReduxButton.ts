import { useEffect, useRef } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import pick from 'lodash/pick'

import { registerButton, removeButton } from '../../ducks/toolbar/store'
import {
    buttonSelector,
} from '../../ducks/toolbar/selectors'
import { State as GlobalState } from '../../ducks/State'
import { ButtonState } from '../../ducks/toolbar/Toolbar'

export type ReduxButtonProps = Partial<Omit<ButtonState, 'buttonId' | 'key'>> & {
    id: string
    entityKey: string
    subMenu?: object[]
}

const REDUX_KEYS: Array<keyof ButtonState> = [
    'isInit',
    'visible',
    'label',
    'size',
    'color',
    'title',
    'hint',
    'icon',
    'disabled',
    'loading',
    'error',
    'conditions',
    'key',
    'buttonId',
    'count',
    'message',
    'className',
    'hintPosition',
    'style',
]

export function useReduxButton<Props extends ReduxButtonProps>({
    id: buttonId,
    entityKey: containerId,
    subMenu,
    ...rest
}: Props): ButtonState {
    const initialProps = useRef(rest)

    const dispatch = useDispatch()
    const reduxProps = useSelector((state: GlobalState) => buttonSelector(state, containerId, buttonId))

    useEffect(() => {
        dispatch(registerButton(containerId, buttonId, pick(initialProps.current, REDUX_KEYS)))

        return () => {
            dispatch(removeButton(containerId, buttonId))
        }
    }, [dispatch, containerId, buttonId, initialProps])

    if (!reduxProps.isInit) {
        return {
            buttonId,
            key: containerId,
            ...rest,
        } as ButtonState
    }

    return reduxProps
}

useReduxButton.displayName = 'useReduxButton'
