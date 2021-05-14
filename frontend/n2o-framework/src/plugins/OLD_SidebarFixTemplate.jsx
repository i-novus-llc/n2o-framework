import React from 'react'
import classNames from 'classnames'

// eslint-disable-next-line import/no-named-as-default
import SideBar from './SideBar/SideBar'

// eslint-disable-next-line react/prop-types
export default ({ children, className }) => {
    let containsFixedSider = false

    React.Children.forEach(children, (child) => {
        if (child.type === SideBar && child.props.fixed) {
            containsFixedSider = true
        }
    })

    return (
        <div
            className={classNames('n2o-template', className, {
                'fixed-scrollable-container': containsFixedSider,
            })}
        >
            {React.Children.map(children, (child) => {
                if (containsFixedSider) {
                    return React.cloneElement(child, {
                        ...child.props,
                        className: classNames(child.props.className, 'fixed-scrollable'),
                    })
                }

                return child
            })}
        </div>
    )
}
