import React from 'react'

import { LinkTarget } from '../../../../../constants/linkTarget'
import { IItem } from '../../../../utils'

import { OuterLink } from './OuterLink'
import { LinkBody } from './LinkBody'
import { InnerLink } from './InnerLink'

interface ILink {
    item: IItem
    className?: string
    active: boolean
}

export function Link(props: ILink) {
    const { item, className, active } = props

    const { target: propsTarget, href, linkType } = item
    const target = propsTarget === LinkTarget.Application ? LinkTarget.Self : propsTarget

    if (linkType === 'outer') {
        return (
            <OuterLink className={className} href={href} target={target}>
                <LinkBody {...item} />
            </OuterLink>
        )
    }

    return (
        <InnerLink className={className} active={active} href={href} target={target}>
            <LinkBody {...item} />
        </InnerLink>
    )
}
