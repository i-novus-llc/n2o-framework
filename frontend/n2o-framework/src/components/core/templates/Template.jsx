import React from 'react'
import PropTypes from 'prop-types'

import { ConfigContainer } from '../../../plugins/Menu/MenuContainer'

import { Layout } from './Layout'

function Template({ children }) {
    return (
        <div className="application">
            <ConfigContainer render={config => <Layout {...config}>{children}</Layout>} />
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
