import React, { ChangeEvent, KeyboardEvent, ReactNode, useState } from 'react'
import uniqueId from 'lodash/uniqueId'
import classNames from 'classnames'
import { UncontrolledTooltip } from 'reactstrap'

import { Input } from '../../Input'

export type Props = {
    checked: boolean,
    className: string,
    disabled: boolean,
    invalid?: boolean,
    label: ReactNode,
    labelClassname?: string,
    name: string,
    onChange(event: ChangeEvent<HTMLInputElement>): void,
    onKeyDown?(evt: KeyboardEvent<HTMLInputElement>): void,
    tooltip?: string,
    value: string | number
    visible?: boolean
}

export function InputRadio({
    label,
    checked,
    disabled,
    visible,
    onChange,
    onKeyDown,
    name,
    value,
    className,
    invalid,
    tooltip,
    labelClassname,
}: Props) {
    const [id] = useState(uniqueId('n2o-radio-'))
    const [target] = useState(uniqueId('n2o-radio-'))

    if (visible === false) {
        return null
    }

    return (
        <div className="n2o-radio-input-wrapper">
            <Input
                className="alt-radio"
                disabled={disabled}
                type="radio"
                checked={checked}
                onChange={onChange}
                onKeyDown={onKeyDown}
                id={id}
                name={name}
                value={value}
            />
            <label
                id={target}
                className={classNames('n2o-radio-input', labelClassname || 'n2o-radio-input-default', className, {
                    checked,
                    active: checked,
                    disabled,
                    invalid,
                })}
                htmlFor={id}
            >
                <span>{label}</span>
                {tooltip && <UncontrolledTooltip target={target}>{tooltip}</UncontrolledTooltip>}
            </label>
        </div>
    )
}

InputRadio.defaultProps = {
    checked: false,
    disabled: false,
} as Props
