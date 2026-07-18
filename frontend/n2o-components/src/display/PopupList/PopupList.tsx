import React, { forwardRef } from 'react'
import classNames from 'classnames'

import { PopupItems } from './PopupItems'
import { type PopUpListProps } from './types'

export { type PopUpListProps }

export const PopupList = forwardRef<HTMLDivElement, PopUpListProps>(
    ({
        children,
        style,
        className,
        onScroll,
        ...rest
    }, ref) => {
        return (
            <div className={classNames('n2o-pop-up__wrapper', className)}>
                <div style={style} className="n2o-dropdown-control n2o-pop-up" ref={ref} onScroll={onScroll}>
                    <PopupItems {...rest} />
                </div>
            </div>
        )
    },
)
