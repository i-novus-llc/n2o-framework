import React, { useContext } from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import isNil from 'lodash/isNil'

import { Text } from '../../../../snippets/Text/Text'
import { resolveBadgeProps } from '../../../../snippets/Badge/utils'
import withTooltip from '../../withTooltip'
import { Shape } from '../../../../snippets/Badge/enums'
import { FactoryContext } from '../../../../../core/factory/context'
import { FactoryLevels } from '../../../../../core/factory/factoryLevels'

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
function BadgeCell(props) {
    const {
        id,
        model,
        fieldKey,
        placement,
        text,
        format,
        badgeFormat,
        visible,
        className,
        shape,
        forwardedRef,
    } = props

    const { getComponent } = useContext(FactoryContext)
    const FactoryBadge = getComponent('Badge', FactoryLevels.SNIPPETS)

    if (!visible) { return null }

    const badgeStyle = {
        order: placement === 'right' ? 1 : -1,
        marginLeft: placement === 'right' && 5,
        marginRight: placement === 'left' && 5,
    }
    const badgeText = get(model, fieldKey || id)

    return (
        <span className="d-inline-flex" ref={forwardedRef}>
            <Text text={text} format={format} />
            {FactoryBadge && !isNil(badgeText) && (
                <FactoryBadge
                    {...resolveBadgeProps(
                        {
                            ...props,
                            fieldId: fieldKey || id,
                        },
                        model,
                    )}
                    text={(
                        <Text
                            title={badgeText}
                            text={badgeText}
                            className={className}
                            format={badgeFormat}
                        />
                    )}
                    style={badgeStyle}
                    shape={shape || Shape.Square}
                />
            )}
        </span>
    )
}

BadgeCell.propTypes = {
    /**
     * ID ячейки
     */
    id: PropTypes.string,
    /**
     * Ключ значения в данных
     */
    fieldKey: PropTypes.string,
    /**
     * Модель данных
     */
    model: PropTypes.object,
    /**
     * Расположение текста
     */
    placement: PropTypes.oneOf(['left', 'right']),
    /**
     * Текст
     */
    text: PropTypes.string,
    /**
     * Формат
     */
    format: PropTypes.string,
    /**
     * Форма баджа
     */
    shape: PropTypes.string,
    /**
     * Формат баджа
     */
    badgeFormat: PropTypes.string,
    /**
     * Цвет баджа
     */
    color: PropTypes.oneOf([
        'secondary',
        'primary',
        'danger',
        'success',
        'warning',
        'info',
    ]),
    /**
     * Ключ картинки баджа в данных
     */
    imageFieldId: PropTypes.string,
    /**
     * Расположение картинки баджа
     */
    imagePosition: PropTypes.string,
    /**
     * Форма картинки баджа
     */
    imageShape: PropTypes.string,
    /**
     * Флаг видимости
     */
    visible: PropTypes.bool,
    className: PropTypes.string,
}

BadgeCell.defaultProps = {
    model: {},
    color: 'secondary',
    placement: 'right',
    visible: true,
}

export default withTooltip(BadgeCell)
