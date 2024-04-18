import React, { useState } from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import uniqueId from 'lodash/uniqueId'
import isString from 'lodash/isString'
import map from 'lodash/map'
import get from 'lodash/get'

import withTooltip from '../../withTooltip'

/**
 * CollapsedCell
 * @reactProps {object} model - модель
 * @reactProps {string} fieldKey - поле данных
 * @reactProps {string} color - цвет элементов
 * @reactProps {string} amountToGroup - количество элементов для группировки
 */

function CollapsedCell(props) {
    const [collapsed, setCollapsed] = useState(true)

    const { visible } = props

    if (!visible) { return null }

    const {
        model,
        fieldKey,
        color,
        amountToGroup,
        labelFieldId,
        forwardedRef,
    } = props

    const changeVisibility = (e) => {
        e.stopPropagation()
        e.preventDefault()

        setCollapsed(!collapsed)
    }

    const data = model[fieldKey] || []
    const items = collapsed ? data.slice(0, amountToGroup) : data
    const text = collapsed ? 'еще' : 'скрыть'

    return (
        <section ref={forwardedRef}>
            {map(items, item => (
                <React.Fragment key={uniqueId('collapsed-cell')}>
                    <span className={classNames('badge', `badge-${color}`)}>
                        {isString(item) ? item : get(item, labelFieldId)}
                    </span>
                    {' '}
                </React.Fragment>
            ))}
            {data.length > amountToGroup && (
                <button
                    type="button"
                    onClick={changeVisibility}
                    className="collapsed-cell-control link-button"
                >
                    {text}
                </button>
            )}
        </section>
    )
}

CollapsedCell.propTypes = {
    /**
     * Модель даных
     */
    model: PropTypes.object.isRequired,
    /**
     * Ключ значения из модели
     */
    fieldKey: PropTypes.string.isRequired,
    /**
     * Цвет
     */
    color: PropTypes.string,
    /**
     * Количество элементов для группировки
     */
    amountToGroup: PropTypes.number,
    /**
     * Ключ label из модели
     */
    labelFieldId: PropTypes.string,
    /**
     * Флаг видимости
     */
    visible: PropTypes.bool,
}

CollapsedCell.defaultProps = {
    amountToGroup: 3,
    color: 'secondary',
    visible: true,
}

export default withTooltip(CollapsedCell)
