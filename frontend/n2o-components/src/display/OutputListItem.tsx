import React from 'react'
import get from 'lodash/get'
import { Link } from 'react-router-dom'

import { Text } from '../Typography/Text'

type OutputListItemProps = {
    isLast: boolean,
    labelFieldId?: string,
    linkFieldId?: string,
    separator?: string,
    target?: string,
}

const renderLink = (isInner: boolean, href: string, target: string, label: string) => {
    if (isInner) {
        return (
            <Link
                to={href}
                className="n2o-output-list__item n2o-output-list__item--link"
                target={target}
            >
                <Text>{label}</Text>
            </Link>
        )
    }

    return (
        <a
            href={href}
            className="n2o-output-list__item n2o-output-list__item--link"
            target={target}
        >
            <Text>{label}</Text>
        </a>
    )
}

const renderText = (label: string) => (
    <span className="n2o-output-list__item"><Text>{label}</Text></span>
)

export const OutputListItem = (props: OutputListItemProps) => {
    const { labelFieldId = 'name', linkFieldId = 'href', target = '_blank', separator = '', isLast } = props

    const label = get(props, labelFieldId)
    const href = get(props, linkFieldId)
    const isLink = !!href
    const isInnerLink = target === 'application'

    return (
        <li>
            {isLink ? renderLink(isInnerLink, href, target, label) : renderText(label)}
            <span className="white-space-pre">{!isLast ? separator : ''}</span>
        </li>
    )
}
