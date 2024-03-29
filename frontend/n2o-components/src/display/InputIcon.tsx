import React, { ReactNode } from 'react'
import classNames from 'classnames'

import { TBaseProps } from '../types'

type Props = TBaseProps & {
    children: ReactNode,
    clickable: boolean,
    hoverable: boolean,
    onClick(): void,
}

export const InputIcon = ({ hoverable, clickable, onClick, children }: Props) => (
    <span
        className={classNames('n2o-input-icon', { hoverable, clickable })}
        onClick={onClick}
    >
        {children}
    </span>
)

InputIcon.defaultProps = {
    hoverable: false,
    clickable: false,
} as Props
