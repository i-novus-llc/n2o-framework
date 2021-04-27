import React from 'react'
import PropTypes from 'prop-types'
import pure from 'recompose/pure'

/**
 * Заголовок таблицы
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {node} children - элемент потомок компонента TableHeader
 */
class TableHeader extends React.Component {
    render() {
        const { className, style, children } = this.props

        return (
            <thead className={className} style={style}>
                {children}
            </thead>
        )
    }
}

TableHeader.propTypes = {
    /* Default props */
    className: PropTypes.string,
    style: PropTypes.string,
    children: PropTypes.node,
}

TableHeader = pure(TableHeader)
export default TableHeader
