import React from 'react'
import isEmpty from 'lodash/isEmpty'
import PropTypes from 'prop-types'

import SecurityController from './SecurityController'

/**
 * Вспомогательная функция для SecurityCheck
 * @param config
 * @param component
 * @param rest
 * @returns {*}
 * @constructor
 */
function SecurityNotRender({ component, config, ...rest }) {
    return isEmpty(config) ? (
        component
    ) : (
        <SecurityController
            config={config}
            {...rest}
        >
            {component}
        </SecurityController>
    )
}

SecurityNotRender.propTypes = {
    authProvider: PropTypes.func,
    config: PropTypes.object,
    user: PropTypes.object,
    onPermissionsSet: PropTypes.func,
    component: PropTypes.any,
}

export default SecurityNotRender
