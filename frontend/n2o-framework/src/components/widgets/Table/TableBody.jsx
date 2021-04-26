import React from 'react'
import PropTypes from 'prop-types'
import pure from 'recompose/pure'

/**
 * Тело таблицы
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {node} children - элемент потомок компонента TableBody
 */
class TableBody extends React.Component {
    render() {
        const { className, style, children } = this.props
        return (
            <tbody className={className} style={style}>
                {children}
            </tbody>
        )
    }
}

TableBody.propTypes = {
    /* Default props */
    className: PropTypes.string,
    style: PropTypes.string,
    children: PropTypes.node,
}

TableBody = pure(TableBody)
export default TableBody
