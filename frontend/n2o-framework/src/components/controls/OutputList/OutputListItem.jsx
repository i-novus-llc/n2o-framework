import React from 'react'
import get from 'lodash/get'
import classNames from 'classnames'
import { Link } from 'react-router-dom'
import PropTypes from 'prop-types'

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
                    className={classNames('n2o-output-list__item', 'n2o-output-list__item--link')}
                    target={target}
                >
                    {label}
                </Link>
            ) : (
                <a
                    href={href}
                    target={target}
                    className={classNames('n2o-output-list__item', {
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
OutputListItem.propTypes = {
    labelFieldId: PropTypes.string,
    linkFieldId: PropTypes.string,
    isLast: PropTypes.bool,
    separator: PropTypes.string,
    target: PropTypes.string,
}

export default OutputListItem
