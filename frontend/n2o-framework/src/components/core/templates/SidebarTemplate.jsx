import React from 'react'
import PropTypes from 'prop-types'

import Footer from '../../../plugins/Footer/Footer'
// eslint-disable-next-line import/no-named-as-default
import SideBar from '../../../plugins/SideBar/SideBar'
// eslint-disable-next-line import/no-named-as-default
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

AppWithSideBar.propTypes = {
    children: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.node),
        PropTypes.node,
    ]),
}
export default AppWithSideBar
