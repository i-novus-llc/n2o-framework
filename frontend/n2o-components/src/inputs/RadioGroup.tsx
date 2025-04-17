import React, { ChangeEvent, KeyboardEvent } from 'react'
import classNames from 'classnames'
import toString from 'lodash/toString'
import isNil from 'lodash/isNil'

import { TBaseInputProps, TBaseProps, TOption } from '../types'
import { EMPTY_ARRAY, NOOP_FUNCTION } from '../utils/emptyTypes'

import { InputRadio } from './Radio'

export type Props = TBaseProps & Omit<TBaseInputProps<string | number>, 'onChange'> & {
    enabledFieldId?: string,
    groupClassName?: string,
    inline?: boolean,
    onChange?(event: ChangeEvent<HTMLInputElement>): void,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    options?: Array<TOption<string | number>>
}

export function RadioGroup({
    value = '',
    visible = true,
    style,
    className: propClassName,
    groupClassName,
    disabled = false,
    options = EMPTY_ARRAY,
    name = '',
    onChange = NOOP_FUNCTION,
    onKeyDown,
    enabledFieldId = '',
    inline = false,
}: Props) {
    const renderedOptions = options.map((radio) => {
        const isDisabled = radio.disabled || (!isNil(radio[enabledFieldId as keyof TOption<string | number>])
            ? !radio[enabledFieldId as keyof TOption<string | number>]
            : disabled)

        return (
            <InputRadio
                {...radio}
                key={radio.value}
                name={name}
                disabled={isDisabled}
                checked={toString(radio.value) === toString(value)}
                onChange={onChange}
                onKeyDown={onKeyDown}
            />
        )
    })

    const className = classNames(
        propClassName,
        'n2o-radio-group',
        groupClassName || 'n2o-radio-group-default',
        inline ? 'n2o-radio-group-inline' : 'n2o-radio-group-vertical',
    )

    if (!visible) { return null }

    return <section className={className} style={style}>{renderedOptions}</section>
}
