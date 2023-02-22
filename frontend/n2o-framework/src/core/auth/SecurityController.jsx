import React, { useState, useEffect, useContext, useRef, useCallback } from 'react'
import { useStore } from 'react-redux'
import isFunction from 'lodash/isFunction'
import set from 'lodash/set'
import omit from 'lodash/omit'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'

import { resolveLinksRecursively } from '../../utils/linkResolver'

import { SecurityContext } from './SecurityProvider'

const excludedKeys = ['checkSecurity', 'config']

export const Behavior = {
    HIDE: 'hide',
    DISABLE: 'disable',
}

const useSecurityController = ({ config = {}, onPermissionsSet, disabled, ...rest }) => {
    const { getState } = useStore()
    const prevResolvedConfig = useRef(null)
    const [hasAccess, setHasAccess] = useState(isEmpty(config) || null)
    const { user, checkSecurity, params } = useContext(SecurityContext)

    const checkPermissions = useCallback(async (config) => {
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
    }, [checkSecurity, onPermissionsSet])

    useEffect(() => {
        if (!isEmpty(config)) {
            checkPermissions(config)
        }
    }, [user, params, config])

    useEffect(() => {
        if (!isEmpty(config)) {
            const store = getState()
            const resolvedConfig = resolveLinksRecursively(config, store)

            if (!isEqual(prevResolvedConfig.current, resolvedConfig)) {
                checkPermissions(resolvedConfig)

                prevResolvedConfig.current = resolvedConfig
            }
        }
    })

    const behaviorDisable = config.behavior === Behavior.DISABLE

    const setDisabled = hasAccess === false && behaviorDisable
    const needRender = behaviorDisable || hasAccess
    const props = omit(rest, excludedKeys)

    if (setDisabled || disabled) {
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

export function SecurityController({ children, ...rest }) {
    const { props, hasAccess, needRender, checkPermissions } = useSecurityController(rest)

    if (isFunction(children)) {
        return children({ ...props, needRender, hasAccess, checkPermissions })
    }

    return needRender ? React.cloneElement(children, props) : null
}

export default SecurityController
