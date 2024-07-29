import React, { createElement, useCallback, type FC, useState, useRef, useMemo } from 'react'
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
import { HotKeys } from 'react-hotkeys/cjs'

type ControlProps<Value = unknown, OnChangeValue = unknown> = {
    control: {
        component: FC<unknown>
    },
    value: Value
    onChange(value: OnChangeValue): void
    onBlur?(): void
}

const eventsMap = { events: 'enter' }

export const Control = ({ control, value, onBlur, onChange }: ControlProps) => {
    const { component, ...otherProps } = control
    const [innerValue, setInnerValue] = useState(value)
    const refSendValue = useRef<unknown>(null)

    refSendValue.current = innerValue

    const handleBlur = useCallback(() => {
        onChange(refSendValue.current)
        onBlur?.()
    }, [onBlur, onChange])

    const handleChange = useCallback((value) => {
        refSendValue.current = value
        setInnerValue(value)
    }, [])

    const handlersMap = useMemo(() => ({
        events: handleBlur,
    }), [handleBlur])

    return (
        <div className="n2o-editable-cell-control" onClick={event => event.stopPropagation()}>
            <HotKeys keyMap={eventsMap} handlers={handlersMap}>
                {/* eslint-disable-next-line @typescript-eslint/no-explicit-any */}
                {createElement<any>(component, {
                    ...otherProps,
                    className: 'n2o-advanced-table-edit-control',
                    onChange: handleChange,
                    onBlur: handleBlur,
                    value: innerValue,
                    autoFocus: true,
                    openOnFocus: true,
                    showButtons: false,
                    resetOnNotValid: false,
                    strategy: 'fixed',
                })}
            </HotKeys>
        </div>
    )
}
