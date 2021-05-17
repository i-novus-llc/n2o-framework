import React from 'react'
import PropTypes from 'prop-types'
import { pure } from 'recompose'

// eslint-disable-next-line import/no-cycle,import/no-named-as-default
import OverlayPages from './OverlayPages'
// eslint-disable-next-line import/no-named-as-default
import GlobalAlerts from './GlobalAlerts'

function Root({ children }) {
    return (
        <>
            <GlobalAlerts />
            {children}
            <OverlayPages />
        </>
    )
}

Root.propTypes = {
    children: PropTypes.node,
}

export default pure(Root)
