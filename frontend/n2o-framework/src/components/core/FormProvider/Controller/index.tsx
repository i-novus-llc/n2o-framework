import { ChangeEvent, useCallback, VFC } from 'react'

import { useFormContext } from '../hooks/useFormContext'
import { useWatch } from '../hooks/useWatch'

type HTMLOnEvent = (event: ChangeEvent<HTMLFormElement>) => void

type ControllerType = {
    name: string,
    render(args: {
        field: {
            onChange: HTMLOnEvent,
            onBlur: HTMLOnEvent,
            onFocus: HTMLOnEvent,
            value: unknown
        }
    }): JSX.Element
}

export const Controller: VFC<ControllerType> = ({ render, name }) => {
    const { setValue, setFocus, setBlur } = useFormContext()
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

    return render({
        field: { onChange, onBlur, onFocus, value: fieldValue },
    })
}

Controller.displayName = 'Controller'
