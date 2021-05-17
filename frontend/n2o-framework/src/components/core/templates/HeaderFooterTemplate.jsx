import React from 'react'
import PropTypes from 'prop-types'

import SimpleHeader from '../../../plugins/Header/SimpleHeader/SimpleHeader'
import Footer from '../../../plugins/Footer/Footer'
// eslint-disable-next-line import/no-named-as-default
import MenuContainer from '../../../plugins/Menu/MenuContainer'

function HeaderFooterTemplate({ children }) {
    return (
        <div className="application">
            <MenuContainer render={config => <SimpleHeader {...config} />} />
            <div className="application-body container-fluid">{children}</div>
            <Footer />
        </div>
    )
}

HeaderFooterTemplate.propTypes = {
    children: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.node),
        PropTypes.node,
    ]),
}

export default HeaderFooterTemplate
