import React from 'react'
import { connect } from 'react-redux'
import { compose } from 'recompose'
import isEqual from 'lodash/isEqual'
import omit from 'lodash/omit'
import PropTypes from 'prop-types'

import { resolveLinksRecursively } from '../../utils/linkResolver'

import withSecurity from './withSecurity'

const excludedKeys = ['checkSecurity', 'config']

/**
 * <SecurityCheck config={{roles: ['admin'], context: ['ivanov']]}}
 *                render={(permissions, err) => permissions ? <Input /> : <Alert message={err.message} />}
 */
class SecurityCheck extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            permissions: null,
            // eslint-disable-next-line react/no-unused-state
            error: null,
        }
    }

    async componentDidMount() {
        await this.checkPermissions(this.props)
    }

    // eslint-disable-next-line react/no-deprecated
    componentWillReceiveProps(nextProps) {
        const { user, config, store } = this.props
        const resolvedConfig = resolveLinksRecursively(config, store)
        const nextResolvedConfig = resolveLinksRecursively(config, nextProps.store)

        if (
            !isEqual(nextProps.user, user) ||
            !isEqual(nextProps.config, config) ||
            !isEqual(nextResolvedConfig, resolvedConfig)
        ) {
            this.checkPermissions(nextProps, nextResolvedConfig)
        }
    }

    async checkPermissions(params, resolvedConfig) {
        const { checkSecurity } = params
        const { onPermissionsSet } = this.props

        try {
            const hasAccess = await checkSecurity(resolvedConfig)

            this.setState(
                // eslint-disable-next-line react/no-unused-state
                { permissions: hasAccess, error: null },
                () => onPermissionsSet && onPermissionsSet(hasAccess),
            )
        } catch (error) {
            this.setState(
                // eslint-disable-next-line react/no-unused-state
                { permissions: null, error },
                () => onPermissionsSet && onPermissionsSet(null),
            )
        }
    }

    render() {
        const { permissions } = this.state
        const { render } = this.props
        const props = omit(this.props, excludedKeys)

        return render({ permissions, ...props })
    }
}

SecurityCheck.propTypes = {
    store: PropTypes.object,
    user: PropTypes.object,
    config: PropTypes.object,
    // eslint-disable-next-line react/no-unused-prop-types
    checkSecurity: PropTypes.func,
    render: PropTypes.func,
    onPermissionsSet: PropTypes.func,
}

SecurityCheck.defaultProps = {
    store: {},
    onPermissionsSet: () => {},
}

const mapStateToProps = state => ({
    store: state,
})

export default compose(withSecurity, connect(mapStateToProps, null))(SecurityCheck)
