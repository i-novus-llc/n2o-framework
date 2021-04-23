/**
 * Created by emamoshin on 09.10.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'

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
class Tab extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            wasActive: false,
        }
    }

    static getDerivedStateFromProps(nextProps) {
        if (nextProps.active) {
            return {
                wasActive: true,
            }
        }
    }

    /**
   * Базовый рендер
   */
    render() {
        const { className, active, children } = this.props
        const { wasActive } = this.state
        const tabStyle = { paddingTop: 2, paddingBottom: 2 }
        return (
            <div className={cx('tab-pane', className, { active })} style={tabStyle}>
                {children}
            </div>
        )
    }
}

Tab.propTypes = {
    /**
   * ID таба
   */
    id: PropTypes.string.isRequired,
    /**
   * Заголовок таба
   */
    title: PropTypes.string,
    /**
   * Иконка
   */
    icon: PropTypes.string,
    /**
   * Флаг активности
   */
    disabled: PropTypes.bool,
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
    /**
   * Флаг активного в данный момент таба
   */
    active: PropTypes.bool,
    /**
   * Класс
   */
    className: PropTypes.string,
    children: PropTypes.node,
}

Tab.defaultProps = {
    disabled: false,
    visible: true,
}

export default Tab
