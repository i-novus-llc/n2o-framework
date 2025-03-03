import React from 'react'
import classNames from 'classnames'
import isUndefined from 'lodash/isUndefined'
import get from 'lodash/get'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'
import { Icon } from '@i-novus/n2o-components/lib/display/Icon'

import { withTooltip } from '../../withTooltip'

import { type Props, ICON_POSITION } from './types'

/**
 * Ячейка таблицы с иконкой
 * @reactProps {string} id - id ячейки
 * @reactProps {object} model - модель строки
 * @reactProps {string} icon - класс иконки
 * @reactProps {string} iconPosition - расположение кнопки относительно текста
 * @reactProps {string | undefined} className - дополнительный класс для ячейки
 */
function IconCellBody({
    id,
    model,
    icon,
    forwardedRef,
    style,
    className,
    iconPosition = ICON_POSITION.LEFT,
    visible = true,
}: Props) {
    if (!visible) { return null }
    const text = get(model, id) as string

    return (
        <div
            ref={forwardedRef}
            className={classNames('icon-cell-container', className, {
                'icon-cell-container__with-tooltip': !isUndefined(model.tooltipFieldId),
                'icon-cell-container__text-left': iconPosition === ICON_POSITION.RIGHT,
            })}
        >
            {icon && <Icon name={icon} style={style} />}
            {text && <div className="n2o-cell-text"><Text>{text}</Text></div>}
        </div>
    )
}

export const IconCell = withTooltip(IconCellBody)
export default IconCell
