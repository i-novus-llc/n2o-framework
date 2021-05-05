import React from 'react'
import isString from 'lodash/isString'
import PropTypes from 'prop-types'
import { Panel as BasePanel } from 'rc-collapse'
import cx from 'classnames'

/**
 * Панель Collapse
 * @param {string} header - Заголовок панели
 * @param {string} type - тип отображения панели( 'default', 'line', 'divider' )
 * @param {boolean} showArrow - показать иконку
 * @param {object} openAnimation - обьект для изменения анимации открытия и закрытия панели
 * @param {boolean} disabled - сделать панель неактивным
 * @param {boolean} forceRender - Рендерить неактивные панели
 * @param {boolean} collapsible -  флаг выключения возможности сворачивания
 * @returns {*}
 * @constructor
 */
const Panel = ({
    className,
    headerClass,
    header,
    type,
    children,
    collapsible,
    ...rest
}) => (
    <BasePanel
        header={(
            <span
                tabIndex={1}
                title={isString(header) && header}
                className="n2o-panel-header-text"
            >
                {header}
            </span>
        )}
        className={cx('n2o-collapse-panel', type, className)}
        headerClass={cx('n2o-panel-header', headerClass, {
            'n2o-disabled': !collapsible,
        })}
        {...rest}
    >
        {children}
    </BasePanel>
)

Panel.propTypes = {
    /**
   * Заголовок панели
   */
    header: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
    /**
   * Класс заголовка
   */
    headerClass: PropTypes.string,
    /**
   * Флаг показа иконки chevron рядом с заголовком панели
   */
    showArrow: PropTypes.bool,
    className: PropTypes.string,
    style: PropTypes.object,
    /**
   * Обьект для изменения анимации открытия и закрытия панели
   */
    openAnimation: PropTypes.object,
    /**
   * Флаг активности панели
   */
    disabled: PropTypes.bool,
    /**
   * Флаг рендера неактивных панелей
   */
    forceRender: PropTypes.bool,
    children: PropTypes.node,
    /**
   * Тип панели
   */
    type: PropTypes.oneOf(['default', 'line', 'divider']),
    /**
   * Флаг выключения возможности сворачивания
   */
    collapsible: PropTypes.bool,
}

Panel.defaultProps = {
    type: 'default',
    collapsible: true,
}

export default Panel
