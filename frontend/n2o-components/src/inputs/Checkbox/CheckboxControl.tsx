import React, { ChangeEvent } from 'react'

import { Checkbox, Props as CheckboxProps } from './Checkbox'

type Props = CheckboxProps & {
    defaultUnchecked: boolean | null,
}

export function CheckboxControl(props: Props) {
    const {
        defaultUnchecked: propsDefaultUnchecked,
        onChange: propsOnChange } = props

    const onChange = (event: ChangeEvent<HTMLInputElement>) => {
        const value = (event.nativeEvent.target as HTMLInputElement).checked
        const defaultUnchecked = propsDefaultUnchecked && null

        if (propsOnChange) {
            propsOnChange(typeof value === 'boolean' ? value : defaultUnchecked)
        }
    }

    return (
        <Checkbox {...props} onChange={onChange} />
    )
}
