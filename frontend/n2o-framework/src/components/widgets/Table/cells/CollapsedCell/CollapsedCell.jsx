import React from 'react'
import PropTypes from 'prop-types'
import cx from 'classnames'
import uniqueId from 'lodash/uniqueId'
import isString from 'lodash/isString'
import map from 'lodash/map'

import withTooltip from '../../withTooltip'

/**
 * CollapsedCell
 * @reactProps {object} model - модель
 * @reactProps {string} fieldKey - поле данных
 * @reactProps {string} color - цвет элементов
 * @reactProps {string} amountToGroup - количество элементов для группировки
 */

class CollapsedCell extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            collapsed: true,
        }

        this._changeVisibility = this._changeVisibility.bind(this)
    }

    _changeVisibility(e) {
        e.stopPropagation()
        e.preventDefault()
        this.setState(prevState => ({ collapsed: !prevState.collapsed }))
    }

    render() {
        const {
            model,
            fieldKey,
            color,
            amountToGroup,
            labelFieldId,
            visible,
        } = this.props

        const data = model[fieldKey] || []
        const items = this.state.collapsed ? data.slice(0, amountToGroup) : data
        const isButtonNeeded = data.length > amountToGroup
        const buttonTitle = this.state.collapsed ? 'еще' : 'скрыть'
        const labelClasses = cx('badge', `badge-${color}`)

        return (
            visible && (
                <>
                    {map(items, item => (
                        <React.Fragment key={uniqueId('collapsed-cell')}>
                            <span className={labelClasses}>
                                {isString(item) ? item : item[labelFieldId]}
                            </span>
                            {' '}
                        </React.Fragment>
                    ))}
                    {isButtonNeeded && (
                        <a
                            href="#"
                            onClick={this._changeVisibility}
                            className="collapsed-cell-control"
                        >
                            {buttonTitle}
                        </a>
                    )}
                </>
            )
        )
    }
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
