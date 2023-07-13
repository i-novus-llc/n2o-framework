import React from 'react'
import classNames from 'classnames'

import { LinkTarget } from '../../../../../../constants/linkTarget'
import { IItem } from '../../Item'

import { OuterLink } from './OuterLink'
import { LinkBody } from './LinkBody'
import { InnerLink } from './InnerLink'

interface ILink {
    active: boolean
    className?: string
    item: IItem
}

export function Link(props: ILink) {
    const {
        active,
        className: linkClassName,
        item,
    } = props

    const {
        className: itemClassName,
        href,
        linkType,
        target: propsTarget,
    } = item

    const target = propsTarget === LinkTarget.Application ? LinkTarget.Self : propsTarget

    if (linkType === 'outer') {
        return (
            <OuterLink className={classNames(linkClassName, itemClassName)} href={href} target={target}>
                <LinkBody {...item} />
            </OuterLink>
        )
    }

    return (
        <InnerLink className={classNames(linkClassName, itemClassName)} active={active} href={href} target={target}>
            <LinkBody {...item} />
        </InnerLink>
    )
}
