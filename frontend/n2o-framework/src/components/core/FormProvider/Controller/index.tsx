import { ChangeEvent, useCallback, VFC } from 'react'

import { useFormContext } from '../hooks/useFormContext'
import { useWatch } from '../hooks/useWatch'
import { Severity, ValidationResult } from '../../../../core/validation/types'

type HTMLOnEvent = (event: ChangeEvent<HTMLFormElement>) => void

type ControllerType = {
    name: string,
    render(args: {
        field: {
            onChange: HTMLOnEvent
            onBlur: HTMLOnEvent
            onFocus: HTMLOnEvent
            onMessage(reason: Error | ValidationResult | null): void
            value: unknown
        }
    }): JSX.Element
}

export const Controller: VFC<ControllerType> = ({ render, name }) => {
    const { setValue, setFocus, setBlur, setMessage } = useFormContext()
    const fieldValue = useWatch({ name })

    const onChange = useCallback<HTMLOnEvent>((event: ChangeEvent<HTMLFormElement>) => {
        let value

        if (event?.target) {
            value = event.target.value
        } else {
            value = event
        }

        setValue(name, value)
    }, [name, setValue])

    const onBlur = useCallback<HTMLOnEvent>(() => {
        setBlur(name)
    }, [name, setBlur])

    const onFocus = useCallback<HTMLOnEvent>(() => {
        setFocus(name)
    }, [name, setFocus])

    const onMessage = useCallback((reason) => {
        if (!reason) {
            setMessage(name, null)
        }

        const message = reason instanceof Error
            ? { text: reason.message, severity: Severity.danger }
            : reason

        setMessage(name, message)
    }, [name, setMessage])

    return render({
        field: { onChange, onBlur, onFocus, value: fieldValue, onMessage },
    })
}

Controller.displayName = 'Controller'
