import React from 'react'
import classNames from 'classnames'

import { InputSelect } from '../InputSelect/InputSelect'
import { type InputSelectProps } from '../InputSelect/types'

export function AutoComplete({ className, ...rest }: InputSelectProps) {
    return (
        <InputSelect
            {...rest}
            className={classNames(className, 'n2o-autocomplete')}
            multiSelect
            enableCustomTags
        />
    )
}
