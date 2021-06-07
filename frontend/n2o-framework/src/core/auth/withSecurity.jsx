import React from 'react'
import PropTypes from 'prop-types'
import { compose, getContext } from 'recompose'
import { createStructuredSelector } from 'reselect'
import { connect } from 'react-redux'

import { userSelector } from '../../ducks/user/selectors'

const withSecurity = (WrappedComponent) => {
    function Security(props) {
        return <WrappedComponent {...props} />
    }

    Security.propTypes = {}
    Security.defaultProps = {}

    const mapStateToProps = createStructuredSelector({
        user: userSelector,
    })

    return compose(
        getContext({
            authProvider: PropTypes.func,
        }),
        connect(mapStateToProps),
    )(Security)
}

export default withSecurity
