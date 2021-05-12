import React from 'react'
import PropTypes from 'prop-types'
import pure from 'recompose/pure'

/**
 * Заголовок таблицы
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {node} children - элемент потомок компонента TableHeader
 */
// eslint-disable-next-line react/prefer-stateless-function
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

// eslint-disable-next-line no-class-assign
TableHeader = pure(TableHeader)
export default TableHeader
