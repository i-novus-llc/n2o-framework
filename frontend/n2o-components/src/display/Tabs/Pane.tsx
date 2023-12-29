import classNames from 'classnames'
import React from 'react'

import { RadioGroup } from '../../inputs/RadioGroup'
import { TOption } from '../../types'

interface Props {
    onChange(event: React.ChangeEvent<HTMLInputElement>): void
    className?: string
    tabs: Array<TOption<string>> | null
    active: string
}

// TODO переделать нормально панеь табов, отвязать от радио-группы
export function Pane({ onChange, className, tabs, active }: Props) {
    if (!tabs) {
        return null
    }

    const options = tabs.map(tab => ({
        ...tab,
        labelClassname: 'n2o-radio-input-tabs',
    }))

    return (
        <RadioGroup
            className={classNames('tabs__list n2o-radio-group-tabs', className)}
            options={options}
            onChange={onChange}
            value={active}
            inline
        />
    )
}
