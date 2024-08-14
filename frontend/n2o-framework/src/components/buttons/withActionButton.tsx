import React, { useCallback, useContext, useMemo, useRef } from 'react'
import { useDispatch, useSelector, useStore } from 'react-redux'
import isNil from 'lodash/isNil'

import { validate as validateDatasource } from '../../core/validation/validate'
import { ExpressionContext } from '../../core/Expression/Context'
import { useResolved } from '../../core/Expression/useResolver'
import { id as getID } from '../../utils/id'
import { ModelPrefix } from '../../core/datasource/const'
import { mergeMeta } from '../../ducks/api/utils/mergeMeta'
import { State as GlobalState } from '../../ducks/State'
import { Action } from '../../ducks/Action'
import { ButtonState } from '../../ducks/toolbar/Toolbar'
import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'

import { ActionButton } from './ActionButton'
import { useReduxButton, ReduxButtonProps } from './useReduxButton'

type ButtonProps = {
    id: string
    visible?: boolean,
    disabled: boolean,
    count?: string,
    conditions: unknown
}

type ActionButtonProps = ReduxButtonProps & {
    id: string
    entityKey: string
    subMenu?: ButtonProps[]
    validate?: string[]
    action?: Action
    model: ModelPrefix
    datasource: string
}

type UseActionProps = ButtonState & {
    validate?: string[]
    action?: Action
    model: ModelPrefix
    datasource: string
}

type EventHandler = (event: MouseEvent, props: UseActionProps, state: GlobalState) => void

const emptyHandler = () => undefined

function useAction({ validate, ...rest }: UseActionProps, onClick: EventHandler) {
    const store = useStore()
    const dispatch = useDispatch()
    const props = useRef(rest)
    const elementId = useMemo(() => (getID()), [])
    const evalContext = useRef({})

    evalContext.current = useContext(ExpressionContext)
    props.current = rest

    const checkValid = useCallback(async () => {
        if (!validate?.length) {
            return true
        }

        let valid = true

        for (const dataSource of validate) {
            const isDataSourceValid = await validateDatasource(store.getState(), dataSource, ModelPrefix.active, dispatch, true)

            valid = valid && isDataSourceValid
        }

        return valid
    }, [validate, store, dispatch])

    const onClickHandler = useCallback(async (event: MouseEvent) => {
        const valid = await checkValid()

        if (!valid) {
            event.preventDefault()
            event.stopPropagation()

            return
        }

        /* необходимо для позиционирования popover */
        const { action, buttonId, key } = props.current

        const extendedAction = action ? mergeMeta(action, {
            target: elementId,
            key,
            buttonId,
            evalContext: evalContext.current,
        }) : undefined

        const state = store.getState()

        onClick(event, {
            ...props.current,
            action: extendedAction,
            // @ts-ignore FIXME убрать отсюда: нужен где-то дальше в цепочке вызовов
            dispatch,
        }, state)
    }, [props, checkValid, store, elementId, onClick, dispatch, evalContext])

    return {
        ...rest,
        id: elementId,
        onClick: onClickHandler,
        // FIXME убрать отсюда: нужен где-то дальше в цепочке вызовов
        dispatch,
    }
}

export default function withActionButton({ onClick = emptyHandler }: { onClick?: EventHandler } = {}) {
    return (WrappedComponent: Parameters<typeof ActionButton>[0]['Component']) => {
        function WithActionButton(props: ActionButtonProps) {
            const reduxProps = useReduxButton(props)

            const withExtendedAction = useAction({
                ...props,
                ...reduxProps,
            }, onClick)

            const { model: prefix, datasource } = props
            const model = useSelector(getModelByPrefixAndNameSelector(prefix, datasource))
            /* TODO брать label & hint для резолва из reduxProps
             * как костыль берём его из пропсов только потому что кнопки в форме уже зарезолвены на уровне филда,
             * и если брать редаксовое значение, то не будет работать обновлиние по модели
             */
            const { hint: propsHint, label: propsLabel, color: propsColor } = props
            const { hint, label, color } = useResolved<Pick<ActionButtonProps, 'hint' | 'label' | 'color'>>({
                hint: propsHint,
                label: propsLabel,
                color: propsColor,
            }, model)

            // FIXME проверить нужно ли это и снести, либо описать зачем
            const { visible, disabled } = props
            const { visible: visibleFromState, disabled: disabledFromState } = withExtendedAction
            const isVisible = !isNil(visible) ? visible : visibleFromState
            const isDisabled = !isNil(disabled) ? disabled : disabledFromState

            const currentMessage = isDisabled ? withExtendedAction.message || hint : hint

            const buttonProps = {
                ...withExtendedAction,
                visible: isVisible,
                disabled: isDisabled,
                hint: currentMessage,
                label,
                placement: withExtendedAction.hintPosition,
                color,
                // url,
            }

            return (
                <ActionButton Component={WrappedComponent} {...buttonProps} />
            )
        }

        return WithActionButton
    }
}
