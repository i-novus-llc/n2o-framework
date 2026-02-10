import React from 'react'
import { Link } from '@i-novus/n2o-components/lib/navigation/Link'
import classNames from 'classnames'

import { useLink } from '../core/router/useLink'

import { useLinkPropsResolver } from './useLinkPropsResolver'
import { type NavigationLinkProps } from './types'

export function NavigationLink({
    target,
    ...props
}: NavigationLinkProps) {
    const {
        url: href,
        visible,
        disabled,
        className,
        ...resolved
    } = useLinkPropsResolver(props)
    const { active, ...linkProps } = useLink({ href, disabled, target })

    if (!visible) { return null }

    return (
        <Link
            className={classNames(className, { active })}
            disabled={disabled}
            {...linkProps}
            {...resolved}
        />
    )
}
