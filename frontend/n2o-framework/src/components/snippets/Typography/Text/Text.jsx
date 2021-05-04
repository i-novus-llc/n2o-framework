import React from 'react'
import PropTypes from 'prop-types'
import cn from 'classnames'

import Base from '../Base'

function Text({ preLine, className, ...rest }) {
    const tag = 'span'

    return (
        <Base
            tag={tag}
            className={cn(className, {
                'white-space-pre-line': preLine,
            })}
            {...rest}
        />
    )
}

Text.propTypes = {
    preLine: PropTypes.bool,
}

Text.defaultProps = {
    preLine: false,
}

export default Text
