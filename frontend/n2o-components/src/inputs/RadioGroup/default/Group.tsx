import React, { ChangeEvent, FC, KeyboardEvent } from 'react'
import classNames from 'classnames'
import toString from 'lodash/toString'
import isNil from 'lodash/isNil'

import { TBaseInputProps, TBaseProps, TOption } from '../../../types'

import { InputRadio, Props as InputProps } from './Input'

export type Props = TBaseProps & Omit<TBaseInputProps<string | number>, 'onChange'> & {
    InputComponent?: FC<InputProps>,
    enabledFieldId: string,
    groupClassName?: string,
    inline: boolean,
    onChange(event: ChangeEvent<HTMLInputElement>): void,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    options: Array<TOption<string | number>>
}

export function Group({
    value,
    visible,
    style,
    className: propClassName,
    groupClassName,
    disabled,
    options,
    name,
    onChange,
    onKeyDown,
    enabledFieldId,
    inline,
    InputComponent: PropInputComponent,
}: Props) {
    const renderedOptions = options.map((radio) => {
        const isDisabled = radio.disabled || (!isNil(radio[enabledFieldId as keyof TOption<string | number>])
            ? !radio[enabledFieldId as keyof TOption<string | number>]
            : disabled)

        const InputComponent = PropInputComponent || InputRadio

        return (
            <InputComponent
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

    return (
        <>
            {visible && (
                <div
                    className={className}
                    style={style}
                >
                    {renderedOptions}
                </div>
            )}
        </>
    )
}

Group.defaultProps = {
    visible: true,
    onChange: () => {},
    disabled: false,
    name: '',
    options: [],
    value: '',
    enabledFieldId: '',
    inline: false,
} as Props
