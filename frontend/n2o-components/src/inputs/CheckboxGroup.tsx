import React, { useMemo, FC } from 'react'
import some from 'lodash/some'
import isNull from 'lodash/isNull'
import isNil from 'lodash/isNil'

import { TBaseInputProps, TBaseProps } from '../types'
import { EMPTY_ARRAY, NOOP_FUNCTION } from '../utils/emptyTypes'

import { Checkbox } from './Checkbox/Checkbox'

const isIncludes = (
    collection: Props['value'],
    object: Props['value'],
    key: Props['valueFieldId'],
) => some(collection, item => item[key] === object[key])

// eslint-disable-next-line @typescript-eslint/no-explicit-any
type Props = TBaseProps & TBaseInputProps<any> & {
    enabledFieldId: string,
    inline: boolean,
    labelFieldId: string,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    options: any[],
    valueFieldId: string,
    visible?: boolean
}

export const CheckboxGroup: FC<Props> = ({
    id = '',
    name = '',
    value = EMPTY_ARRAY,
    inline = false,
    valueFieldId = 'value',
    enabledFieldId = 'enabled',
    labelFieldId = 'label',
    visible = true,
    options = EMPTY_ARRAY,
    disabled = false,
    style,
    className,
    onChange = NOOP_FUNCTION,
    onBlur = NOOP_FUNCTION,
    onFocus = NOOP_FUNCTION,
}) => {
    function getDisabled(option: { disabled?: boolean, [key: string]: unknown }) {
        if (disabled) { return true }
        if (isNil(option[enabledFieldId])) { return !!option.disabled }

        return !option[enabledFieldId]
    }

    const renderedOptions = useMemo(
        () => options.map(option => (
            <Checkbox
                key={option[valueFieldId]}
                value={option}
                label={option[labelFieldId]}
                checked={!isNull(value) && value && isIncludes(value, option, valueFieldId)}
                disabled={getDisabled(option)}
                inline={inline}
                onChange={onChange}
                onBlur={onBlur}
                onFocus={onFocus}
            />
        )),
        // eslint-disable-next-line react-hooks/exhaustive-deps
        [options, value, valueFieldId, disabled, inline, onChange, onBlur, onFocus],
    )

    if (!visible) { return null }

    return <div className={className} style={style}>{renderedOptions}</div>
}

CheckboxGroup.displayName = 'CheckboxGroup'
