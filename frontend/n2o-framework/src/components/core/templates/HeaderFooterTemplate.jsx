import React from 'react'

import SimpleHeader from '../../../plugins/Header/SimpleHeader/SimpleHeader'
import Footer from '../../../plugins/Footer/Footer'
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

export default HeaderFooterTemplate
