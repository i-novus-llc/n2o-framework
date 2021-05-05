import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

function FixedContainer({ className, setRef, fixed, width, style, children }) {
    const ref = fixed ? setRef : undefined

    return (
        <div
            className={className}
            ref={ref}
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
    setRef: PropTypes.func,
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
