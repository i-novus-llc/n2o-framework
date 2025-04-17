import React, { ReactNode, CSSProperties } from 'react'
import classNames from 'classnames'

import { EMPTY_OBJECT } from '../../../utils/emptyTypes'

export interface Props {
    className?: string
    fixed: boolean
    width?: string | number
    style?: CSSProperties
    children: ReactNode
}

export function FixedContainer({ children, className, fixed, width = 'auto', style = EMPTY_OBJECT }: Props) {
    return (
        <div className={className} style={{ width, height: fixed ? style.height : 'auto' }}>
            <div
                style={fixed ? style : {}}
                className={classNames('n2o-page__fixed-container', { 'n2o-page__fixed-container--fixed': fixed })}
            >
                {children}
            </div>
        </div>
    )
}

export default FixedContainer
