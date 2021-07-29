import React from 'react'
import PropTypes from 'prop-types'

import { ConfigContainer } from '../../../plugins/Menu/MenuContainer'

import { Page } from './Page'

function Template({ children }) {
    return (
        <div className="application">
            <ConfigContainer render={config => <Page {...config}>{children}</Page>} />
        </div>
    )
}

Template.propTypes = {
    children: PropTypes.oneOfType([
        PropTypes.arrayOf(PropTypes.node),
        PropTypes.node,
    ]),
}

export default Template
