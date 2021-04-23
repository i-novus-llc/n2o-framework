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
 * @reactProps {string} className
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
            this.props.select(day, inputName)
        }
    }

    /**
   * Проверка на то, что пришедшая из пропсов дата - сегодня
   * @param props
   */
    componentWillReceiveProps(props) {
        this.setState({
            current: props.day.isSame(moment(), 'day'),
        })
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
    className: '',
}

Day.propTypes = {
    day: PropTypes.instanceOf(moment).isRequired,
    disabled: PropTypes.bool,
    selected: PropTypes.bool,
    otherMonth: PropTypes.bool,
    inputName: PropTypes.string,
    current: PropTypes.string,
    select: PropTypes.func,
    className: PropTypes.string,
}

export default Day
