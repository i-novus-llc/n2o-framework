import React, { useCallback, useState, useEffect } from 'react'

type NumberPickerButtonProps = {
    children: JSX.Element,
    disabled?: boolean,
    onClick(): void
}

export const NumberPickerButton = ({ onClick, disabled, children }: NumberPickerButtonProps) => {
    const [pressed, setPressed] = useState(false)

    useEffect(() => {
        let timerId: number

        if (pressed) {
            timerId = setInterval(onClick, 200)
        }

        return () => clearTimeout(timerId)
    }, [pressed, onClick])

    useEffect(() => {
        if (disabled) {
            setPressed(false)
        }
    }, [disabled, setPressed])

    const onDownHandler = useCallback(() => {
        setPressed(true)
    }, [setPressed])

    const onUpHandler = useCallback(() => {
        setPressed(false)
    }, [setPressed])

    return (
        <button
            type="button"
            className="n2o-number-picker__button"
            onClick={onClick}
            onMouseDown={onDownHandler}
            onMouseUp={onUpHandler}
            onMouseLeave={onUpHandler}
            disabled={disabled}
        >
            {children}
        </button>
    )
}
