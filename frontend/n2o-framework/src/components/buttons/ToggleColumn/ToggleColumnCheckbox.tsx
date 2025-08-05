import React, { useEffect, useRef } from 'react'
import classNames from 'classnames'
import { Checkbox as Component } from '@i-novus/n2o-components/lib/inputs/Checkbox/Checkbox'

import { type ToggleColumnCheckboxProps } from './types'

export function ToggleColumnCheckbox({ className, label, enabled = true, indeterminate = false, checked = false }: ToggleColumnCheckboxProps) {
    const ref = useRef<HTMLInputElement>(null)

    useEffect(() => {
        if (ref.current) { ref.current.indeterminate = indeterminate }
    }, [indeterminate])

    return (
        <Component
            inputRef={ref}
            className={classNames(className, { checked, indeterminate })}
            label={label}
            checked={checked}
            disabled={!enabled}
        />
    )
}
