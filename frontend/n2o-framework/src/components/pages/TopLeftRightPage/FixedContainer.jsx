import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

function FixedContainer({ className, fixed, width, style, children }) {
    return (
        <div
            className={className}
            style={{
                width,
                height: fixed && style.height,
            }}
        >
            <div
                className={classNames('n2o-page__fixed-container', {
                    'n2o-page__fixed-container--fixed': fixed,
                })}
                style={fixed ? style : {}}
            >
                {children}
            </div>
        </div>
    )
}

FixedContainer.propTypes = {
    className: PropTypes.string,
    fixed: PropTypes.string,
    width: PropTypes.object,
    style: PropTypes.object,
    children: PropTypes.oneOfType([
        PropTypes.func,
        PropTypes.node,
        PropTypes.element,
    ]),
}

FixedContainer.defaultProps = {
    width: 'auto',
    style: {},
}

export default FixedContainer
