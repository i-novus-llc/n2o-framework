import classNames from 'classnames'
import React from 'react'

import { Icon } from '../Icon'

export const renderExpandIcon = (isActive: boolean, collapsible = true) => {
    if (!collapsible) { return null }

    return (
        <div className="n2o-collapse-icon-wrapper collapse-icon-wrapper">
            <Icon className={classNames('collapse-icon', { isActive })} name="fa fa-angle-right" />
        </div>
    )
}
