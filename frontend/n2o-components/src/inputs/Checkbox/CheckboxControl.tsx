import React, { ChangeEvent } from 'react'

import { Checkbox, Props as CheckboxProps } from './Checkbox'

type Props = CheckboxProps & {
    defaultUnchecked: string,
}

export function CheckboxControl(props: Props) {
    const { defaultUnchecked: propsDefaultUnchecked, onChange: propsOnChange } = props

    const onChange = (event: ChangeEvent<HTMLInputElement>) => {
        let defaultUnchecked

        switch (propsDefaultUnchecked) {
            case 'null':
                defaultUnchecked = null

                break

            case 'false':
            default:
                defaultUnchecked = false
        }
        const value = (event.nativeEvent.target as HTMLInputElement).checked

        propsOnChange?.(value || defaultUnchecked)
    }

    return (
        <Checkbox {...props} onChange={onChange} />
    )
}
