import React, { ReactNode } from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../types'

type Props = TBaseProps & {
    children: ReactNode,
    clickable: boolean,
    hoverable: boolean,
    onClick(): void,
}

export const InputIcon = ({
    hoverable = false,
    clickable = false,
    onClick,
    children,
}: Props) => (
    <span
        className={classNames('n2o-input-icon', { hoverable, clickable })}
        onClick={onClick}
    >
        {children}
    </span>
)
