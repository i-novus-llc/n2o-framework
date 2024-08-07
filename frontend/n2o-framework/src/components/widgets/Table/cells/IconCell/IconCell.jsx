import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import isUndefined from 'lodash/isUndefined'
import get from 'lodash/get'

import { Text } from '../../../../snippets/Text/Text'
// eslint-disable-next-line import/no-named-as-default
import Icon from '../../../../snippets/Icon/Icon'
import withTooltip from '../../withTooltip'

import { textPlaceTypes } from './cellTypes'

/**
 * Ячейка таблицы с иконкой
 * @reactProps {string} id - id ячейки
 * @reactProps {object} model - модель строки
 * @reactProps {string} icon - класс иконки
 * @reactProps {string} iconPosition - расположение кнопки относительно текста
 * @reactProps {string | undefined} className - дополнительный класс для ячейки
 */
function IconCell({ id, model, visible, icon, iconPosition, forwardedRef, style, className }) {
    if (!visible) {
        return null
    }
    const text = get(model, id)

    return (
        visible && (
            <div
                ref={forwardedRef}
                className={classNames('icon-cell-container', className, {
                    'icon-cell-container__with-tooltip': !isUndefined(model.tooltipFieldId),
                    'icon-cell-container__text-left': iconPosition === textPlaceTypes.RIGHT,
                })}
            >
                {icon && <Icon name={icon} style={style} />}
                {text && <div className="n2o-cell-text"><Text text={text} /></div>}
            </div>
        )
    )
}

IconCell.propTypes = {
    /**
     * ID ячейки
     */
    id: PropTypes.string.isRequired,
    /**
     * Модель данных
     */
    model: PropTypes.object.isRequired,
    /**
     * Иконка
     */
    icon: PropTypes.string.isRequired,
    /**
     * Местоположение текста
     */
    iconPosition: PropTypes.oneOf(Object.values(textPlaceTypes)),
    /**
     * Флаг видимости
     */
    visible: PropTypes.bool,

    className: PropTypes.string,
}

IconCell.defaultProps = {
    iconPosition: textPlaceTypes.LEFT,
    visible: true,
}

export { IconCell }

export default withTooltip(IconCell)
