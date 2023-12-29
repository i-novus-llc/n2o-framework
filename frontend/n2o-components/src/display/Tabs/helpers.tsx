import React from 'react'
import classNames from 'classnames'

import { TOption } from '../../types'

import { Tab } from './Tabs'

export function createOptions(tabs: Tab[], hideSingleTab?: boolean): Array<TOption<string>> | null {
    if (hideSingleTab && tabs.length === 1) {
        return null
    }

    return tabs.map((tab) => {
        const { id, label, icon, disabled, tooltip, className, visible } = tab

        return {
            className: classNames('tabs-nav-item', className),
            value: id,
            id,
            disabled,
            tooltip,
            visible,
            label: (
                <>
                    {icon && <span className={icon} />}
                    {' '}
                    {label}
                </>
            ),
        }
    })
}
