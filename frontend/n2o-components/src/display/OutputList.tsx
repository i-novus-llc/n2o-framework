import React, { ComponentType } from 'react'
import map from 'lodash/map'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'
import classNames from 'classnames'

import { TBaseInputProps, TBaseProps } from '../types'
import { EMPTY_ARRAY } from '../utils/emptyTypes'
import { LinkProps, LinkTarget } from '../navigation/types'
import { Link } from '../navigation/Link'
import { Text } from '../Typography/Text'

import '../styles/components/OutputList.scss'

export type Props = TBaseProps & TBaseInputProps<Array<Record<string, unknown>>> & {
    direction?: 'row' | 'column'
    labelFieldId?: string
    linkFieldId?: string
    separator?: string
    target?: LinkTarget
    LinkComponent?: ComponentType<LinkProps>
}

export const OutputList = ({
    className,
    direction = 'column',
    value = EMPTY_ARRAY,
    labelFieldId = 'name',
    LinkComponent = Link,
    linkFieldId = 'href',
    separator = '',
    target,
}: Props) => (
    <ul className={classNames('n2o-output-list', className, `n2o-output-list--${direction}`)}>
        {map(value, (item, index) => {
            if (isEmpty(item)) { return null }
            const label = get(item, labelFieldId) as string | undefined
            const href = get(item, linkFieldId) as string | undefined
            const isLast = index === value.length - 1

            return (
                <li key={index}>
                    <LinkComponent
                        url={href}
                        className={classNames('n2o-output-list__item', { 'n2o-output-list__item--link': !!href })}
                        target={target}
                    >
                        <Text>{label}</Text>
                    </LinkComponent>
                    { !isLast && <span className="white-space-pre">{separator}</span> }
                </li>
            )
        })}
    </ul>
)
