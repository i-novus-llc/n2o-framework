import React, { Component } from 'react'
import get from 'lodash/get'
import set from 'lodash/set'
import isEqual from 'lodash/isEqual'
import PropTypes from 'prop-types'
import { compose, getContext } from 'recompose'
import { createStructuredSelector } from 'reselect'
import { connect } from 'react-redux'

import { userSelector } from '../../ducks/user/selectors'

import { SECURITY_CHECK } from './authTypes'

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

export function withSecurityList(WrappedComponent, path) {
    class Security extends Component {
        state = {
            itemsWithSuccessfulSecurity: [],

        }

        async componentDidMount() {
            await this.checkSecurity()
        }

        async componentDidUpdate(prevProps) {
            const { user } = this.props

            if (!isEqual(prevProps.user, user) ||
                    !isEqual(get(prevProps, path), get(this.props, path))) {
                await this.checkSecurity()
            }
        }

        isSecurityCheckRequired = () => {
            const securityList = get(this.props, path, [])

            return securityList.some(({ security }) => security)
        }

        checkSecurity = async () => {
            const { authProvider, user } = this.props

            const securityList = get(this.props, path)

            const itemsWithSuccessfulSecurity = []

            for (const item of securityList) {
                const { security } = item

                try {
                    await authProvider(SECURITY_CHECK, {
                        config: security,
                        user,
                    }).then(() => {
                        itemsWithSuccessfulSecurity.push(item)
                    })
                } catch (e) {
                    // ...
                }
            }

            this.setState({ itemsWithSuccessfulSecurity })

            return null
        }

        resolveProps = (props) => {
            const { itemsWithSuccessfulSecurity } = this.state

            const finalProps = { ...props }

            return set(finalProps, path, itemsWithSuccessfulSecurity)
        }

        render() {
            const resolvedProps = this.isSecurityCheckRequired()
                ? this.resolveProps(this.props)
                : this.props

            return <WrappedComponent {...resolvedProps} />
        }
    }

    Security.propTypes = {
        authProvider: PropTypes.func,
        user: PropTypes.object,
    }

    return withSecurity(Security)
}

export default withSecurity
