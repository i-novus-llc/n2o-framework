import React, { useContext } from 'react'

import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { Tags } from '../../snippets/Tags/Tags'

import { type MultiSelectProps, type InputComponentProps } from './types'

export function MultiSelect({
    value,
    disabled,
    onInput,
    onBlur,
    onFocus,
    placeholder,
    inputRef,
    inputComponentRef,
    tagsRef,
    className,
    onInputClick,
    onKeyDown,
    postfix,
    onTagRemove,
    tags,
    maxTagTextLength,
    readOnly,
    id,
}: MultiSelectProps) {
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
            className={className}
            onClick={onInputClick}
            postfix={postfix}
            onKeyDown={onKeyDown}
            readOnly={readOnly}
            id={String(id)}
        >
            <Tags tags={tags} onTagRemove={onTagRemove} ref={tagsRef} maxTagTextLength={maxTagTextLength} />
        </FactoryInput>
    )
}
