/**
 * Created by emamoshin on 09.10.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'

/**
 * Нав Таба
 * @reactProps {string} className - css-класс
 * @reactProps {object} style - стили
 * @reactProps {node} children - элемент потомок компонента TabNav
 */
class TabNav extends React.Component {
    render() {
        const { className, style, children } = this.props
        return (
            <ul className={cx('nav', 'nav-tabs', className)} style={{ ...style }}>
                {children}
            </ul>
        )
    }
}

TabNav.propTypes = {
    className: PropTypes.string,
    style: PropTypes.object,
    children: PropTypes.node,
}

export default TabNav
