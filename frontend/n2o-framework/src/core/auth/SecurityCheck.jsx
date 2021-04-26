import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { getContext, compose } from 'recompose'
import { createStructuredSelector } from 'reselect'
import isEqual from 'lodash/isEqual'
import omit from 'lodash/omit'

import { userSelector } from '../../selectors/auth'
import { dataRequestWidget } from '../../actions/widgets'

import { SECURITY_CHECK } from './authTypes'

/**
 * <SecurityCheck config={{roles: ['admin'], context: ['ivanov']]}}
 *                render={(permissions, err) => permissions ? <Input /> : <Alert message={err.message} />}
 */
class SecurityCheck extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            permissions: null,
            error: null,
        }
    }

    async componentDidMount() {
        await this.checkPermissions(this.props)
    }

    componentWillReceiveProps(nextProps) {
        if (
            !isEqual(nextProps.user, this.props.user) ||
      !isEqual(nextProps.config, this.props.config)
        ) {
            this.checkPermissions(nextProps)
        }
    }

    async checkPermissions(params) {
        const { authProvider, config, user } = params
        const { onPermissionsSet } = this.props
        try {
            const permissions = await authProvider(SECURITY_CHECK, {
                config,
                user,
            })
            this.setState(
                { permissions, error: null },
                () => onPermissionsSet && onPermissionsSet(permissions),
            )
        } catch (error) {
            this.setState(
                { permissions: null, error },
                () => onPermissionsSet && onPermissionsSet(null),
            )
        }
    }

    render() {
        const { permissions } = this.state
        const props = omit(this.props, ['authProvider', 'config'])
        return this.props.render({ permissions, ...props })
    }
}

SecurityCheck.propTypes = {
    authProvider: PropTypes.func,
    config: PropTypes.object,
    user: PropTypes.object,
    render: PropTypes.func,
}

SecurityCheck.defaultProps = {
    onPermissionsSet: () => {},
}

const mapStateToProps = createStructuredSelector({
    user: userSelector,
})

export default compose(
    getContext({
        authProvider: PropTypes.func,
    }),
    connect(mapStateToProps),
)(SecurityCheck)
