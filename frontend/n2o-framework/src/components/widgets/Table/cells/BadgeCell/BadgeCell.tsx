import React, { useContext } from 'react'
import get from 'lodash/get'
import isNil from 'lodash/isNil'
import { FormattedText } from '@i-novus/n2o-components/lib/Typography/FormattedText'

import { resolveBadgeProps } from '../../../../snippets/Badge/utils'
import { withTooltip } from '../../withTooltip'
import { Position, Shape } from '../../../../snippets/Badge/enums'
import { FactoryContext } from '../../../../../core/factory/context'
import { FactoryLevels } from '../../../../../core/factory/factoryLevels'
import { EMPTY_OBJECT } from '../../../../../utils/emptyTypes'

import { type Props } from './types'

/**
 * Ячейка таблицы типа бейдж
 * @reactProps {string} id
 * @reactProps {object} model - модель данных
 * @reactProps {string} fieldKey - ключ модели для этой ячейки
 * @reactProps {string} text - текст бейджа
 * @reactProps {string} placement - положение бейджа("left" или "right")
 * @reactProps {string} color - цветовая схема бейджа(["default", "danger", "success", "warning", "info")
 * @reactProps {string} shape - форма бейджа("square" или "rounded" или "circle")
 * @reactProps {string} imageFieldId - ключ модели для картинки бейджа
 * @reactProps {string} imagePosition - положение картинки бейджа("left" или "right")
 * @reactProps {string} imageShape - форма картинки бейджа("square" или "rounded" или "circle")
 * @example
 * <BadgeCell model={model} filedKey={'name'} text="info"/>
 */
function BadgeCellBody({
    id,
    fieldKey,
    text,
    format,
    badgeFormat,
    className,
    shape,
    forwardedRef,
    colorFieldId,
    imageFieldId,
    imagePosition,
    imageShape,
    model = EMPTY_OBJECT,
    color = 'secondary',
    placement = Position.Right,
    visible = true,
}: Props) {
    const { getComponent } = useContext(FactoryContext)
    const FactoryBadge = getComponent('Badge', FactoryLevels.SNIPPETS)

    if (!visible) { return null }

    const badgeText = get(model, fieldKey || id) as string

    return (
        <span className="badge-cell" ref={forwardedRef}>
            <FormattedText format={format}>{text}</FormattedText>
            {FactoryBadge && !isNil(badgeText) && (
                <FactoryBadge
                    {...resolveBadgeProps(
                        {
                            position: placement,
                            shape,
                            text,
                            color,
                            fieldId: fieldKey || id,
                            imagePosition,
                            imageShape,
                            colorFieldId: colorFieldId || '',
                            imageFieldId: imageFieldId || '',
                        },
                        model,
                    )}
                    text={(
                        <span className={className}>
                            <FormattedText format={badgeFormat}>{badgeText}</FormattedText>
                        </span>
                    )}
                    style={{ order: placement === 'right' ? 1 : -1 }}
                    shape={shape || Shape.Square}
                />
            )}
        </span>
    )
}

export const BadgeCell = withTooltip(BadgeCellBody)
export default BadgeCell
