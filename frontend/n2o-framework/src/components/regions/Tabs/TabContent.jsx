import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
/**
 * Контент Таба
 * @reactProps {string} className - css-класс
 * @reactProps {boolean} activeId
 * @reactProps {node} children - элемет потомок компонента TabContent
 * @reactProps {object} style - элемет стили элемента
 */
export function TabContent(props) {
    const { className, children, style, ...rest } = props

    return (
        <div className={classNames('tab-content', className)} style={style}>
            {React.Children.map(children, child => React.cloneElement(child, rest))}
        </div>
    )
}

TabContent.propTypes = {
    className: PropTypes.string,
    activeId: PropTypes.bool,
    children: PropTypes.node,
    style: PropTypes.object,
}

export default TabContent
