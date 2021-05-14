import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
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

        this.changeVisibility = this.changeVisibility.bind(this)
    }

    changeVisibility(e) {
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
        const { collapsed } = this.state

        const data = model[fieldKey] || []
        const items = collapsed ? data.slice(0, amountToGroup) : data
        const isButtonNeeded = data.length > amountToGroup
        const buttonTitle = collapsed ? 'еще' : 'скрыть'
        const labelClasses = classNames('badge', `badge-${color}`)

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

                        // eslint-disable-next-line jsx-a11y/anchor-is-valid
                        <a
                            href="#"
                            onClick={this.changeVisibility}
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
