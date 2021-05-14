/**
 * Created by emamoshin on 29.05.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'

import Regions from './index'

function Region({ src, ...props }) {
    const { children } = props

    return React.createElement(Regions[src], props, children)
}

Region.propTypes = {
    src: PropTypes.string.isRequired,
    containers: PropTypes.object,
    children: PropTypes.any,
}

function mapDispatchToProps(dispatch) {
    return {
        dispatch,
    }
}

export default connect(
    state => state,
    mapDispatchToProps,
)(Region)
