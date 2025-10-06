import React from 'react'
import classNames from 'classnames'

import { StandardButton } from '../StandardButton/StandardButton'
import { type Props } from '../Simple/types'

export function MenuItem({ className, color, ...rest }: Props) {
    return <StandardButton {...rest} className={classNames(className, 'n2o-menu-item')} color={color || 'link'} />
}
