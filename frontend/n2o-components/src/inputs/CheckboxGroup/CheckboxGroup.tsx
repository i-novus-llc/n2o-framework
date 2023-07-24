import React, { useMemo, FC } from 'react'
import some from 'lodash/some'
import isNull from 'lodash/isNull'
import isNil from 'lodash/isNil'

import { TBaseInputProps, TBaseProps } from '../../types'
import { Checkbox } from '../Checkbox/Checkbox'

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
    valueFieldId: string
}

export const CheckboxGroup: FC<Props> = ({
    value,
    disabled,
    inline,
    labelFieldId,
    valueFieldId,
    enabledFieldId,
    options,
    visible,
    style,
    className,
    onChange,
    onBlur,
    onFocus,
}) => {
    const renderedOptions = useMemo(() => options?.map(option => (
        <Checkbox
            key={option[valueFieldId]}
            value={option}
            label={option[labelFieldId]}
            checked={!isNull(value) && value && isIncludes(value, option, valueFieldId)}
            disabled={disabled || isNil(option[enabledFieldId]) ? option.disabled : !option[enabledFieldId]}
            inline={inline}
            onChange={onChange}
            onBlur={onBlur}
            onFocus={onFocus}
        />
    )),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [options, value, valueFieldId, disabled, inline, onChange, onBlur, onFocus])

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

CheckboxGroup.defaultProps = {
    id: '',
    name: '',
    value: [],
    inline: false,
    valueFieldId: '',
    enabledFieldId: '',
    labelFieldId: '',
    visible: true,
    options: [],
    onChange: () => {},
    onFocus: () => {},
    onBlur: () => {},
} as Props
