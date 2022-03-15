import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import { ScrollContainer } from '../../snippets/ScrollContainer/ScrollContainer'

/**
 * Компонент Таб
 * @reactProps {string} className - css-класс
 * @reactProps {string} title - заголовок
 * @reactProps {string} icon - иконка
 * @reactProps {boolean} disabled - флаг активности
 * @reactProps {boolean} active - активен / неактивен таб
 * @reactProps {string} id - id таба
 * @reactProps {node} children - элемент потомок компонента Tab
 */
export function Tab({ className, active, children, fixed }) {
    const ContentWrapper = fixed ? ScrollContainer : 'div'

    return (
        <ContentWrapper
            className={classNames('tab-pane flex-grow-1', className, {
                active,
                'd-none': !active,
                'tab-pane-fixed': fixed,
            })}
        >
            {children}
        </ContentWrapper>
    )
}

Tab.propTypes = {
    active: PropTypes.bool,
    fixed: PropTypes.bool,
    className: PropTypes.string,
    children: PropTypes.node,
}

export default Tab
