import React, { Component } from 'react'
import get from 'lodash/get'
import set from 'lodash/set'
import isEqual from 'lodash/isEqual'
import PropTypes from 'prop-types'
import { compose, getContext } from 'recompose'

const withSecurity = (WrappedComponent) => {
    function Security(props) {
        return <WrappedComponent {...props} />
    }

    Security.propTypes = {}
    Security.defaultProps = {}

    return compose(
        getContext({
            user: PropTypes.object,
            checkSecurity: PropTypes.func,
        }),
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
            const { checkSecurity: checkSecurityFn } = this.props

            const securityList = get(this.props, path)

            const itemsWithSuccessfulSecurity = []

            for (const item of securityList) {
                const { security } = item

                try {
                    await checkSecurityFn(security).then(() => {
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
        checkSecurity: PropTypes.func,
        user: PropTypes.object,
    }

    return withSecurity(Security)
}

export default withSecurity
