import React from 'react'
import cx from 'classnames'

import SideBar from './SideBar/SideBar'

export default ({ children, className }) => {
    let containsFixedSider = false
    const newChildren = React.Children.forEach(children, (child) => {
        if (child.type === SideBar && child.props.fixed) {
            containsFixedSider = true
        }
    })

    return (
        <div
            className={cx('n2o-template', className, {
                'fixed-scrollable-container': containsFixedSider,
            })}
        >
            {React.Children.map(children, (child) => {
                if (containsFixedSider) {
                    return React.cloneElement(child, {
                        ...child.props,
                        className: cx(child.props.className, 'fixed-scrollable'),
                    })
                }

                return child
            })}
        </div>
    )
}
