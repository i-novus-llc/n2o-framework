import React from 'react'
import classNames from 'classnames'

import { LinkTarget } from '../../../../../../components/core/router/types'
import { Item } from '../../../../../CommonMenuTypes'

import { OuterLink } from './OuterLink'
import { LinkBody } from './LinkBody'
import { InnerLink } from './InnerLink'

interface LinkProps {
    active: boolean
    className?: string
    item: Item
}

export function Link({
    active,
    className: linkClassName,
    item,
}: LinkProps) {
    const {
        className: itemClassName,
        href,
        linkType,
        target: propsTarget,
        style,
    } = item

    const target = propsTarget === LinkTarget.application ? LinkTarget.self : propsTarget

    if (linkType === 'outer') {
        return (
            <OuterLink
                className={classNames(linkClassName, itemClassName)}
                href={href}
                target={target}
                style={style}
            >
                <LinkBody {...item} />
            </OuterLink>
        )
    }

    return (
        <InnerLink
            className={classNames(linkClassName, itemClassName)}
            active={active}
            href={href}
            target={target}
            style={style}
        >
            <LinkBody {...item} />
        </InnerLink>
    )
}
