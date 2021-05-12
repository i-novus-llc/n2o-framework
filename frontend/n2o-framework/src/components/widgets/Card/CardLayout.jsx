import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'

/**
 * Лайоут для Карточек
 * @param {node} children - элемент потомок компонента CardLayout
 * @param {string} className - Кастомный класс для компонента
 * @param {...any} rest
 * @returns {*}
 * @constructor
 */
function CardLayout({ children, className, ...rest }) {
    return (
        <div className={cx('n2o-card-wrap', className)} {...rest}>
            {children}
        </div>
    )
}

CardLayout.propTypes = {
    children: PropTypes.node,
    className: PropTypes.string,
}

export default CardLayout
