import React from 'react'
import flowRight from 'lodash/flowRight'
import { maskitoDateTimeOptionsGenerator, maskitoDateOptionsGenerator } from '@maskito/kit'
import { useMaskito } from '@maskito/react'

import { combineRefs } from '../../utils'
import { EMPTY_OBJECT } from '../../../utils/emptyTypes'

import { WithFormat } from './WithFormat'
import { WithEvents } from './WithEvents'
import { WithDecorators } from './WithDecorators'
import { type DateMaskProps } from './types'

function Component({
    className,
    inputClassName,
    disabled,
    value,
    onClick,
    onFocus,
    onBlur,
    onKeyDown,
    onInput,
    ref,
    autoFocus,
    dateMode,
    timeMode,
    dateSeparator,
    min,
    max,
    placeholder,
    prefixComponent = null,
    suffixComponent = null,
    style = EMPTY_OBJECT,
}: DateMaskProps) {
    const options = timeMode
        ? maskitoDateTimeOptionsGenerator({
            dateMode,
            timeMode,
            dateSeparator,
            dateTimeSeparator: ' ',
            min,
            max,
        })
        : maskitoDateOptionsGenerator({
            mode: dateMode,
            separator: dateSeparator,
            min,
            max,
        })

    const maskRef = useMaskito({ options })

    return (
        <div className={className}>
            {prefixComponent}
            <input
                value={value || ''}
                ref={combineRefs(maskRef, ref)}
                className={inputClassName}
                style={style}
                placeholder={placeholder}
                disabled={disabled}
                autoFocus={autoFocus}
                onInput={onInput}
                onFocus={onFocus}
                onBlur={onBlur}
                onKeyDown={onKeyDown}
                onClick={onClick}
            />
            {suffixComponent}
        </div>
    )
}

export const DateMask = flowRight(
    WithFormat<DateMaskProps>,
    WithEvents<DateMaskProps>,
    WithDecorators<DateMaskProps>,
)(Component)
