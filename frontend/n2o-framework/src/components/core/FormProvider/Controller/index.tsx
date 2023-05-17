import { ChangeEvent, useCallback, VFC } from 'react'

import { useFormContext } from '../hooks/useFormContext'
import { useWatch } from '../hooks/useWatch'

type THTMLOnEvent = (event: ChangeEvent<HTMLFormElement>) => void

type TController = {
    name: string,
    render(args: {
        field: {
            onChange: THTMLOnEvent,
            onBlur: THTMLOnEvent,
            onFocus: THTMLOnEvent,
            value: unknown
        }
    }): JSX.Element
}

export const Controller: VFC<TController> = ({ render, name }) => {
    const { setValue, setFocus, setBlur } = useFormContext()
    const fieldValue = useWatch({ name })

    const onChange = useCallback<THTMLOnEvent>((event: ChangeEvent<HTMLFormElement>) => {
        let value

        if (event?.target) {
            value = event.target.value
        } else {
            value = event
        }

        setValue(name, value)
    }, [name, setValue])

    const onBlur = useCallback<THTMLOnEvent>(() => {
        setBlur(name)
    }, [name, setBlur])

    const onFocus = useCallback<THTMLOnEvent>(() => {
        setFocus(name)
    }, [name, setFocus])

    return render({
        field: { onChange, onBlur, onFocus, value: fieldValue },
    })
}

Controller.displayName = 'Controller'
