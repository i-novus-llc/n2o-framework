import React, { MouseEventHandler } from 'react'
import classNames from 'classnames'
import { NavItem } from 'reactstrap'
import { Link as LinkComponent } from '@i-novus/n2o-components/lib/navigation/Link'

import { Item } from '../../../../../CommonMenuTypes'
import { useLink } from '../../../../../../components/core/router/useLink'

import { LinkBody } from './LinkBody'

interface LinkProps {
    active: boolean
    className?: string
    item: Item
    onClick?: MouseEventHandler
}

export function Link({
    className: linkClassName,
    item,
    onClick,
}: LinkProps) {
    const {
        className: itemClassName,
        href,
        target,
        disabled,
    } = item

    const { active, ...linkProps } = useLink({ href, disabled, target, onClick })

    return (
        <NavItem>
            <LinkComponent
                className={classNames(linkClassName, itemClassName, 'nav-link', { active })}
                disabled={disabled}
                {...linkProps}
                label={(<LinkBody {...item} />)}
            />
        </NavItem>
    )
}
