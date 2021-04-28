import React from 'react'
import PropTypes from 'prop-types'
import moment from 'moment'

/**
 * Компонент Day
 * @reactProps {moment} day
 * @reactProps {boolean} disabled
 * @reactProps {boolean} selected
 * @reactProps {boolean} otherMonth
 * @reactProps {string} inputName
 * @reactProps {string} current
 * @reactProps {function} select
 */
class Day extends React.Component {
    constructor(props) {
        super(props)
        this.onClick = this.onClick.bind(this)
    }

    /**
     * Установить новую дату на клик
     */
    onClick() {
        const { day, select, disabled, inputName } = this.props

        if (!disabled) {
            select(day, inputName)
        }
    }

    /**
   * Базовый рендер
   * @returns {*}
   */
    render() {
        const { selected, disabled, day, otherMonth, current } = this.props
        const dis = disabled ? 'disabled' : ''
        const sel = selected ? 'selected' : ''
        const om = otherMonth ? 'other-month' : ''
        const cur = current ? 'current' : ''

        return (
            // eslint-disable-next-line jsx-a11y/no-noninteractive-element-interactions
            <td
                className={`n2o-calendar-day ${dis} ${sel} ${om} ${cur}`}
                onMouseDown={this.onClick}
            >
                {day.format('D')}
            </td>
        )
    }
}

Day.defaultProps = {
    disabled: false,
    selected: false,
}

Day.propTypes = {
    day: PropTypes.instanceOf(moment).isRequired,
    disabled: PropTypes.bool,
    selected: PropTypes.bool,
    otherMonth: PropTypes.bool,
    inputName: PropTypes.string,
    current: PropTypes.string,
    select: PropTypes.func,
}

export default Day
