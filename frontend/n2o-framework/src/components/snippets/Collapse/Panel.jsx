import React from 'react'
import PropTypes from 'prop-types'
import { Panel as BasePanel } from 'rc-collapse'
import classNames from 'classnames'

import Label from '../../widgets/Form/fields/StandardField/Label'
import HelpPopover from '../../widgets/Form/fields/StandardField/HelpPopover'
import { Badge } from '../Badge/Badge'

/**
 * Панель Collapse
 * @param {string} header - Заголовок панели
 * @param {string} help - Подсказка панели
 * @param {string} type - тип отображения панели( 'default', 'line', 'divider' )
 * @param {string} description - description под label
 * @param {boolean} showArrow - показать иконку
 * @param {object} openAnimation - обьект для изменения анимации открытия и закрытия панели
 * @param {boolean} disabled - сделать панель неактивным
 * @param {boolean} forceRender - Рендерить неактивные панели
 * @param {boolean} collapsible -  флаг выключения возможности сворачивания
 * @returns {*}
 * @constructor
 */

function PanelHeader({ header, help, description, badge }) {
    const title = typeof header === 'string' ? header : null

    return (
        <div className="n2o-panel-header-container">
            <Badge {...badge} visible={!!badge}>
                <span title={title} className="n2o-panel-header-text">{header}</span>
            </Badge>
            <HelpPopover help={help} />
            <Label className="n2o-fieldset__description" value={description} />
        </div>
    )
}

export const Panel = ({
    className,
    headerClass,
    header,
    type,
    children,
    collapsible,
    description,
    help,
    badge,
    ...rest
}) => (
    <BasePanel
        header={(
            <PanelHeader
                header={header}
                help={help}
                description={description}
                badge={badge}
            />
        )}
        className={classNames(
            'n2o-collapse-panel',
            type,
            className,
            {
                'with-description': description,
                'with-badge': badge,
            },
        )}
        headerClass={classNames(
            'n2o-panel-header',
            headerClass,
            {
                'n2o-disabled': !collapsible,
                'with-description': description,
            },
        )}
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
    description: PropTypes.string,
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
    /**
     * Подсказка около label
     */
    help: PropTypes.string,
}

Panel.defaultProps = {
    type: 'default',
    collapsible: true,
}

export default Panel
