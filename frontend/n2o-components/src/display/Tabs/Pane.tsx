import classNames from 'classnames'
import React from 'react'

import { Group } from '../../inputs/RadioGroup/tabs/Group'
import { TOption } from '../../types'

interface Props {
    onChange(event: React.ChangeEvent<HTMLInputElement>): void
    className?: string
    options: Array<TOption<string | number>> | null
    active: string
}

export function Pane({ onChange, className, options, active }: Props) {
    if (!options) {
        return null
    }

    return (
        <Group
            className={classNames('tabs__list', className)}
            options={options}
            onChange={onChange}
            value={active}
            inline
        />
    )
}
