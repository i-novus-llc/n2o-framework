import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

const Html = ({ value, visible, disabled, className }) => {
    const disabledStyle = {
        pointerEvents: 'none',
        opacity: '0.4',
    }
    const style = disabled ? disabledStyle : undefined

    return (
        <div style={style} className={classNames('n2o-html', className)}>
            {/* eslint-disable-next-line react/no-danger */}
            {visible && <div dangerouslySetInnerHTML={{ __html: value }} />}
        </div>
    )
}

Html.propTypes = {
    className: PropTypes.string,
    value: PropTypes.string,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
}

Html.defaultProps = {
    visible: true,
    disabled: false,
}

export default Html
