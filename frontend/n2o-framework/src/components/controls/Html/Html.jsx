import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'

const Html = ({ value, visible, disabled, className }) => {
    const disabledStyle = {
        pointerEvents: 'none',
        opacity: '0.4',
    }
    const style = disabled ? disabledStyle : undefined

    return (
        <div style={style} className={cx('n2o-html', className)}>
            {visible && <div dangerouslySetInnerHTML={{ __html: value }} />}
        </div>
    )
}

Html.propTypes = {
    value: PropTypes.string,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
}

Html.defaultProps = {
    visible: true,
    disabled: false,
}

export default Html
