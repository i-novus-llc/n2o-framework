import React, { useState, useEffect, useContext, useRef } from 'react'
import { useSelector } from 'react-redux'
import isFunction from 'lodash/isFunction'
import set from 'lodash/set'
import omit from 'lodash/omit'
import isEqual from 'lodash/isEqual'

import { resolveLinksRecursively } from '../../utils/linkResolver'

import { SecurityContext } from './SecurityProvider'

const excludedKeys = ['checkSecurity', 'config']

export const Behavior = {
    HIDE: 'hide',
    DISABLE: 'disable',
}

const useSecurityController = ({ config = {}, onPermissionsSet, ...rest }) => {
    const store = useSelector(state => state)
    const prevResolvedConfig = useRef(null)
    const [hasAccess, setHasAccess] = useState(null)
    const { user, checkSecurity, params } = useContext(SecurityContext)

    const checkPermissions = async (config) => {
        const setPermissionsCallback = (hasAccess) => {
            if (isFunction(onPermissionsSet)) {
                onPermissionsSet(hasAccess)
            }
        }

        try {
            const hasAccess = await checkSecurity(config)

            setHasAccess(hasAccess)
            setPermissionsCallback(hasAccess)
        } catch (err) {
            setHasAccess(false)
            setPermissionsCallback(false)
        }
    }

    useEffect(() => {
        checkPermissions(config)
    }, [user, params, config])

    useEffect(() => {
        const resolvedConfig = resolveLinksRecursively(config, store)

        if (!isEqual(prevResolvedConfig.current, resolvedConfig)) {
            checkPermissions(resolvedConfig)

            prevResolvedConfig.current = resolvedConfig
        }
    }, [store])

    const behaviorDisable = config.behavior === Behavior.DISABLE

    const setDisabled = !hasAccess && behaviorDisable
    const needRender = behaviorDisable || hasAccess
    const props = omit(rest, excludedKeys)

    if (setDisabled) {
        set(props, 'disabled', true)
        set(props, 'enabled', false)
    }

    return {
        props,
        hasAccess,
        needRender,
        checkPermissions,
    }
}

function SecurityController({ children, ...rest }) {
    const { props, hasAccess, needRender, checkPermissions } = useSecurityController(rest)

    if (isFunction(children)) {
        return children({ ...props, hasAccess, checkPermissions })
    }

    return needRender ? React.cloneElement(children, props) : null
}

export default SecurityController
