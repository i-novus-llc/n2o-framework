import React, { ChangeEventHandler } from 'react'
import TextareaAutosize from 'react-textarea-autosize'
import omit from 'lodash/omit'
import classNames from 'classnames'

import { withRightPlaceholder } from '../helpers/withRightPlaceholder'
import { TBaseInputProps, TBaseProps } from '../types'

type TextAreaProps = TBaseProps & Omit<TBaseInputProps<string>, 'onBlur' | 'onFocus'> & {
    maxRows?: number,
    onChange?: ChangeEventHandler<HTMLTextAreaElement>,
    rows?: number
}

const TextAreaComponent = ({
    className,
    style,
    disabled,
    placeholder,
    rows = 3,
    maxRows = 3,
    value = '',
    onChange,
    ...rest
}: TextAreaProps) => {
    const max = rows > maxRows ? rows : maxRows

    return (
        <TextareaAutosize
            className={classNames('n2o-text-area form-control', className)}
            style={style}
            disabled={disabled}
            placeholder={placeholder}
            minRows={rows}
            maxRows={max}
            value={value}
            onChange={onChange}
            {...omit(rest, 'id')}
        />
    )
}

export const TextArea = withRightPlaceholder(TextAreaComponent)
