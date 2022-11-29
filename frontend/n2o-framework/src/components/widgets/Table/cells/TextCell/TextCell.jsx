/**
 * Created by emamoshin on 27.09.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import classNames from 'classnames'
import isUndefined from 'lodash/isUndefined'

import withTooltip from '../../withTooltip'
import { Text } from '../../../../snippets/Typography/Text/Text'
import { Icon } from '../../../../snippets/Icon/Icon'

import { iconPositions } from './cellTypes'
import SubText from './SubText'

/** Описание */
function TextCell({
    fieldKey,
    icon,
    iconPosition,
    id,
    model,
    preLine,
    subTextFieldKey,
    subTextFormat,
    visible,
    ...rest
}) {
    const mainText = model && get(model, fieldKey || id)

    if ((typeof mainText === 'string' && !mainText) || !visible) {
        return null
    }

    return (
        <div className="d-inline-flex flex-column">
            <div
                className={classNames('icon-cell-container', {
                    'icon-cell-container__with-tooltip': !isUndefined(
                        model.tooltipFieldId,
                    ),
                    'icon-cell-container__text-left': iconPosition === iconPositions.RIGHT,
                })}
            >
                {icon && <Icon name={icon} />}
                <Text
                    text={mainText}
                    preLine={preLine}
                    {...rest}
                />
            </div>
            {subTextFieldKey ? (
                <SubText
                    subText={model && get(model, subTextFieldKey)}
                    format={subTextFormat}
                />
            ) : null}
        </div>
    )
}

TextCell.propTypes = {
    /**
   * Модель данных
   */
    model: PropTypes.object,
    /**
   * Ключ значения из модели
   */
    fieldKey: PropTypes.string,
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Формат
   */
    format: PropTypes.string,
    /**
   * Ключ значения сабтекста из модели
   */
    subTextFieldKey: PropTypes.string,
    /**
   * Формат сабтекста
   */
    subTextFormat: PropTypes.string,
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
    /**
     * Иконка
     */
    icon: PropTypes.string,
    /**
     * Местоположение текста
     */
    iconPosition: PropTypes.oneOf(Object.values(iconPositions)),
    preLine: PropTypes.bool,
    id: PropTypes.string,
}

TextCell.defaultProps = {
    visible: true,
    iconPosition: iconPositions.LEFT,
}

export { TextCell }

export default withTooltip(TextCell)
