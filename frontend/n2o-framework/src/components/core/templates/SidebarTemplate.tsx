import React from 'react'

import Footer from '../../../plugins/Footer/Footer'
import SideBar from '../../../plugins/SideBar/SideBar'
import { ConfigContainer, type ConfigContainerProps } from '../../../plugins/Menu/MenuContainer'

import { type TemplateProps } from './types'

/**
 * Class representing an Application container with {@link SideBar}
 */
export function SidebarTemplate({ children, ...props }: TemplateProps) {
    return (
        <div className="application">
            <div className="body-container">
                <ConfigContainer render={(config: ConfigContainerProps) => <SideBar {...config} {...props} />} />
                <div className="application-body application-body--aside container-fluid">{children}</div>
            </div>
            <Footer />
        </div>
    )
}

export default SidebarTemplate
