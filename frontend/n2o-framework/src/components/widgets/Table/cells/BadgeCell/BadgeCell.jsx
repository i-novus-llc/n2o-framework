/* eslint-disable react/no-unused-prop-types */
import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import isNil from 'lodash/isNil'

import Text from '../../../../snippets/Text/Text'
import { Badge } from '../../../../snippets/Badge/Badge'
import { resolveBadgeProps } from '../../../../snippets/Badge/utils'
import withTooltip from '../../withTooltip'
import { Shape } from '../../../../snippets/Badge/enums'

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
class BadgeCell extends React.Component {
    /**
   * Базовый рендер
   */
    render() {
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
        } = this.props

        const badgeStyle = {
            order: placement === 'right' ? 1 : -1,
            marginLeft: placement === 'right' && 5,
            marginRight: placement === 'left' && 5,
        }
        const badgeText = get(model, fieldKey || id)

        return (
            visible && (
                <span className="d-inline-flex">
                    <Text text={text} format={format} />
                    {!isNil(badgeText) && (
                        <Badge
                            {...resolveBadgeProps(
                                {
                                    ...this.props,
                                    fieldId: fieldKey || id,
                                },
                                model,
                            )}
                            text={(
                                <Text
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
        )
    }
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
