import React from 'react'
import PropTypes from 'prop-types'
import omit from 'lodash/omit'
import classNames from 'classnames'

import getElementType from '../../../tools/getElementType'

/**
 * Ячейка таблицы
 * @reactProps {string} className - имя css класса
 * @reactProps {object} style - css стиль
 * @reactProps {element} component - React класс компонента cell
 * @reactProps {string} as - Тип элемента для рендеринга
 * @reactProps {object} model - Модель строки
 * @reactProps {number} colSpan - colSpan таблицы
 * @reactProps {node} children - элемент потомок компонента TableCell
 * @reactProps {('left'|'center'|'right')} contentAlignment - выравнивание контента в ячейке
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
            contentAlignment,
            needRender = true,
        } = this.props

        if (!needRender) {
            return null
        }

        const ElementType = getElementType(TableCell, this.props)

        const resolveClassname = config => classNames(className, {
            [`content-alignment-${contentAlignment}`]: contentAlignment,
            ...config,
        })

        if (React.Children.count(children)) {
            return (
                <ElementType
                    className={resolveClassname()}
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
                className={resolveClassname({ 'hide-on-blur': hideOnBlur })}
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
    // eslint-disable-next-line react/no-unused-prop-types
    as: PropTypes.string,
    model: PropTypes.object,
    colSpan: PropTypes.number,
    hideOnBlur: PropTypes.bool,
    needRender: PropTypes.bool,
    contentAlignment: PropTypes.oneOf(['left', 'center', 'right']),
}

TableCell.defaultProps = {
    as: 'td',
    hideOnBlur: false,
}

export default TableCell
