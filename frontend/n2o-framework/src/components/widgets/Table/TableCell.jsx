import React from 'react'
import PropTypes from 'prop-types'
import omit from 'lodash/omit'
import cn from 'classnames'

import getElementType from '../../../tools/getElementType'

/**
 * Ячейка таблицы
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {element} component - React класс компонента cell
 * @reactProps {string} as - Тип элемента для рендеринга
 * @reactProps {object} model - Модель строки
 * @reactProps {number} colSpan - colSpan таблицы
 * @reactprops {node} children - элемент потомок компонента TableCell
 */
class TableCell extends React.Component {
    constructor(props) {
        super(props)
        this.getPassProps = this.getPassProps.bind(this)
    }

    getPassProps() {
        return omit(this.props, ['component', 'colSpan', 'as', 'model'])
    }

    render() {
        const {
            className,
            style,
            component,
            colSpan,
            children,
            model,
            hideOnBlur,
        } = this.props
        const ElementType = getElementType(TableCell, this.props)
        if (React.Children.count(children)) {
            return (
                <ElementType
                    className={className}
                    colSpan={colSpan}
                    model={model}
                    style={style}
                >
                    {children}
                </ElementType>
            )
        }

        return (
            <ElementType
                className={cn(className, { 'hide-on-blur': hideOnBlur })}
                colSpan={colSpan}
                style={style}
            >
                {component &&
          React.createElement(component, {
              ...this.getPassProps(),
              model,
          })}
            </ElementType>
        )
    }
}

TableCell.propTypes = {
    /* Default props */
    className: PropTypes.string,
    style: PropTypes.string,
    children: PropTypes.node,
    /* Specific props */
    component: PropTypes.oneOfType([PropTypes.func, PropTypes.element]),
    as: PropTypes.string,
    model: PropTypes.object,
    colSpan: PropTypes.number,
    hideOnBlur: PropTypes.bool,
}

TableCell.defaultProps = {
    as: 'td',
    hideOnBlur: false,
}

export default TableCell
