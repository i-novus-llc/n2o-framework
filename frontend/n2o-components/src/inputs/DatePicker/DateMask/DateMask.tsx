import React from 'react'
import flowRight from 'lodash/flowRight'
import { maskitoDateTimeOptionsGenerator, maskitoDateOptionsGenerator, maskitoEventHandler } from '@maskito/kit'
import { MaskitoPlugin, maskitoUpdateElement } from '@maskito/core'

import { combineRefs } from '../../utils'
import { EMPTY_OBJECT } from '../../../utils/emptyTypes'
import { type onInputProcessorParams, useMask } from '../../helpers/input/useMask'

import { WithFormat } from './WithFormat'
import { WithEvents } from './WithEvents'
import { WithDecorators } from './WithDecorators'
import { type DateMaskProps } from './types'
import { getFloatingPlaceholder } from './helpers'

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
    autocomplete,
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

    const onInputProcessor = (params: onInputProcessorParams) => {
        const { element, placeholder } = params

        if (element.value === placeholder) {
            return maskitoUpdateElement(element, '')
        }

        return null
    }

    const { maskRef, maskedValue } = useMask({
        ...options,
        mask: options.mask,
        placeholder: getFloatingPlaceholder(dateMode, timeMode),
        defaultValue: value,
        processors: { plugins: options.plugins as MaskitoPlugin[] },
        onInputProcessor,
    })

    return (
        <div className={className}>
            {prefixComponent}
            <input
                value={maskedValue}
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
                autoComplete={autocomplete}
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
