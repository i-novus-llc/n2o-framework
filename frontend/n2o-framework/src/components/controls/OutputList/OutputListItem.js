import React from 'react'
import get from 'lodash/get'
import cn from 'classnames'
import { Link } from 'react-router-dom'

function OutputListItem(props) {
    const { labelFieldId, linkFieldId, target, separator, isLast } = props
    const label = <>{get(props, labelFieldId)}</>
    const href = get(props, linkFieldId)
    const isInnerLink = target === 'application'

    return (
        <li>
            {isInnerLink ? (
                <Link
                    to={href}
                    className={cn('n2o-output-list__item', 'n2o-output-list__item--link')}
                    target={target}
                >
                    {label}
                </Link>
            ) : (
                <a
                    href={href}
                    target={target}
                    className={cn('n2o-output-list__item', {
                        'n2o-output-list__item--link': !!href,
                    })}
                >
                    {label}
                </a>
            )}
            <span className="white-space-pre">{!isLast ? separator : ''}</span>
        </li>
    )
}

export default OutputListItem
