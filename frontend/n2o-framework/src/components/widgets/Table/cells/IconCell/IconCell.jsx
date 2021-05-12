import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import isUndefined from 'lodash/isUndefined'

import Text from '../../../../snippets/Text/Text'
// eslint-disable-next-line import/no-named-as-default
import Icon from '../../../../snippets/Icon/Icon'
import withTooltip from '../../withTooltip'

import { iconCellTypes, textPlaceTypes } from './cellTypes'

/**
 * Ячейка таблицы с иконкой
 * @reactProps {string} id - id ячейки
 * @reactProps {object} model - модель строки
 * @reactProps {string} icon - класс иконки
 * @reactProps {string} type - тип ячейки
 * @reactProps {string} textPlace - расположение текста
 */
function IconCell({ id, model, visible, icon, type, textPlace }) {
    const text = model[id]

    return (
        visible && (
            <div
                className={classNames('icon-cell-container', {
                    'icon-cell-container__with-tooltip': !isUndefined(
                        model.tooltipFieldId,
                    ),
                    'icon-cell-container__text-left': textPlace === textPlaceTypes.LEFT,
                })}
            >
                {icon && <Icon name={icon} />}
                {type === iconCellTypes.ICONANDTEXT && (
                    <div className="n2o-cell-text">
                        <Text text={text} />
                    </div>
                )}
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
   * Тип ячейки
   */
    type: PropTypes.oneOf(Object.values(iconCellTypes)),
    /**
   * Местоположение текста
   */
    textPlace: PropTypes.oneOf(Object.values(textPlaceTypes)),
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
}

IconCell.defaultProps = {
    type: iconCellTypes.ICONANDTEXT,
    textPlace: textPlaceTypes.RIGHT,
    visible: true,
}

export { IconCell }

export default withTooltip(IconCell)
