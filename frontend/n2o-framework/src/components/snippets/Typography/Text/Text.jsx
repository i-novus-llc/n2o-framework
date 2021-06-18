import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { Base } from '../Base'

export function Text({ preLine, className, ...rest }) {
    const tag = 'span'

    return (
        <Base
            tag={tag}
            className={classNames(className, {
                'white-space-pre-line': preLine,
            })}
            {...rest}
        />
    )
}

Text.propTypes = {
    preLine: PropTypes.bool,
    className: PropTypes.string,
}

Text.defaultProps = {
    preLine: false,
}

export default Text
