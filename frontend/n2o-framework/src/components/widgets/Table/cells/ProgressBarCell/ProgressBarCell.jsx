import React from 'react'
import PropTypes from 'prop-types'
import Progress from 'reactstrap/lib/Progress'

import withTooltip from '../../withTooltip'

import progressBarStyles from './progressBarStyles'

/**
 * Ячейка таблицы с прогресс баром
 * @reactProps {string} id - id ячейки
 * @reactProps {object} model - модель строки
 * @reactProps {boolean} animated - флаг animated
 * @reactProps {boolean} striped - флаг striped
 * @reactProps {string} color - цвет прогресс бара
 * @reactProps {string} size - размер прогресс бара
 */
function ProgressBarCell({
    id,
    animated,
    striped,
    color,
    size,
    visible,
    model,
}) {
    return (
        visible && (
            <Progress
                value={model[id]}
                className={size}
                animated={animated}
                striped={striped}
                color={color}
            />
        )
    )
}

ProgressBarCell.propTypes = {
    /**
   * ID ячейки
   */
    id: PropTypes.string.isRequired,
    /**
   * Модель данных
   */
    model: PropTypes.object.isRequired,
    /**
   * Флаг анимированности ячейки
   */
    animated: PropTypes.bool,
    /**
   * Флаг штриховки
   */
    striped: PropTypes.bool,
    /**
   * Цвет ячейки
   */
    color: PropTypes.oneOf(Object.values(progressBarStyles)),
    /**
   * Размер ячейки
   */
    size: PropTypes.oneOf(['mini', 'default', 'large']),
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
}

ProgressBarCell.defaultProps = {
    animated: false,
    striped: false,
    size: 'default',
    visible: true,
}

export default withTooltip(ProgressBarCell)
