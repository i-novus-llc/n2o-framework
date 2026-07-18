import React, { useContext } from 'react'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'

import { type SingleSelectProps, type InputComponentProps } from './types'

export function SingleSelect({
    value,
    disabled,
    onInput,
    onBlur,
    onFocus,
    placeholder,
    inputRef,
    inputComponentRef,
    onInputClick,
    className,
    postfix,
    onKeyDown,
    readOnly,
    id,
}: SingleSelectProps) {
    const { getComponent } = useContext(FactoryContext)
    const FactoryInput = getComponent<InputComponentProps>('Inputs/Input', FactoryLevels.SNIPPETS)

    if (!FactoryInput) { return null }

    return (
        <FactoryInput
            value={value}
            disabled={disabled}
            onFocus={onFocus}
            onBlur={onBlur}
            onInput={onInput}
            placeholder={placeholder}
            inputRef={inputRef}
            ref={inputComponentRef}
            onClick={onInputClick}
            className={className}
            postfix={postfix}
            onKeyDown={onKeyDown}
            readOnly={readOnly}
            id={String(id)}
        />
    )
}
