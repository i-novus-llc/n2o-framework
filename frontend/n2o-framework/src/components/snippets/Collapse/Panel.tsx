import React, { useContext, ReactNode } from 'react'
import { Panel as BasePanel } from '@i-novus/n2o-components/lib/display/Panel/Panel'
import classNames from 'classnames'

import Label from '../../widgets/Form/fields/StandardField/Label'
import HelpPopover from '../../widgets/Form/fields/StandardField/HelpPopover'
import { FactoryContext } from '../../../core/factory/context'
import { FactoryLevels } from '../../../core/factory/factoryLevels'
import { Props as BadgeProps } from '../Badge/Badge'

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

interface PanelHeaderProps {
    header: string | ReactNode
    help: string
    description: string
    badge: BadgeProps
}

function PanelHeader({ header, help, description, badge }: PanelHeaderProps) {
    const { getComponent } = useContext(FactoryContext)
    const FactoryBadge = getComponent('Badge', FactoryLevels.SNIPPETS)

    return (
        <div className="n2o-panel-header-container">
            {FactoryBadge && (
                <FactoryBadge {...badge} visible={!!badge}>
                    <span
                        title={typeof header === 'string' ? header : ''}
                        className="n2o-panel-header-text"
                    >
                        {header}
                    </span>
                </FactoryBadge>
            )}
            <HelpPopover help={help} />
            <Label className="n2o-fieldset__description" value={description} />
        </div>
    )
}

export type Props = {
    type: string
    collapsible?: boolean
    badge: BadgeProps
    className?: string
    headerClass?: string
    children: ReactNode
    showArrow?: boolean
    forceRender?: boolean
} & PanelHeaderProps

export const Panel = ({
    className,
    headerClass,
    header,
    children,
    description,
    help,
    badge,
    type = 'default',
    collapsible = true,
    ...rest
}: Props) => (
    <BasePanel
        header={<PanelHeader header={header} help={help} description={description} badge={badge} />}
        className={classNames('n2o-collapse-panel', type, className, {
            'with-description': description,
            'with-badge': badge,
        })}
        headerClass={classNames('n2o-panel-header', headerClass, {
            'n2o-disabled': !collapsible,
            'with-description': description,
        })}
        {...rest}
    >
        {children}
    </BasePanel>
)

export default Panel
