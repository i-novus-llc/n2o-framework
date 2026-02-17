import React from 'react'
import classNames from 'classnames'

import { FactoryStandardButton, type Props, Color } from '../FactoryStandardButton'

export function MenuItem({ className, color, ...rest }: Props) {
    return <FactoryStandardButton {...rest} className={classNames(className, 'n2o-menu-item')} color={color || Color.link} />
}
