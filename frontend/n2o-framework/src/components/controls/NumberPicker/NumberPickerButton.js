import React, { useCallback, useState, useEffect } from 'react'
import PropTypes from 'prop-types'

export function NumberPickerButton({ onClick, disabled, children, className }) {
    const [pressed, setPressed] = useState(false)

    useEffect(() => {
        let timerId

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

NumberPickerButton.propTypes = {
    onClick: PropTypes.func.isRequired,
    disabled: PropTypes.bool.isRequired,
    children: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.node),
        PropTypes.node,
    ]).isRequired,
    className: PropTypes.string,
}
