/**
 * Created by emamoshin on 29.05.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'

import Regions from './index'

class Region extends React.Component {
    render() {
        const { src, ...props } = this.props
        return React.createElement(Regions[src], props, this.props.children)
    }
}

Region.propTypes = {
    src: PropTypes.string.isRequired,
    containers: PropTypes.object,
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
