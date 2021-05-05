import React from 'react'

import Footer from '../../../plugins/Footer/Footer'
import SideBar from '../../../plugins/SideBar/SideBar'
import MenuContainer from '../../../plugins/Menu/MenuContainer'

/**
 * Class representing an Application container with {@link SideBar}
 */
function AppWithSideBar({ children }) {
    return (
        <div className="application">
            <div className="body-container">
                <MenuContainer render={config => <SideBar {...config} />} />
                <div className="application-body application-body--aside container-fluid">
                    {children}
                </div>
            </div>
            <Footer />
        </div>
    )
}

export default AppWithSideBar
