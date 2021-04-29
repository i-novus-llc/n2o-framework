import React from 'react'
import PropTypes from 'prop-types'
import moment from 'moment'
import onClickOutside from 'react-onclickoutside'
import { useTranslation } from 'react-i18next'

// eslint-disable-next-line import/no-cycle
import Calendar from './Calendar'

/**
 * Компонент PopUp
 * @reactProps {moment} value
 * @reactProps {string} dateFormat
 * @reactProps {string} timeFormat
 * @reactProps {function} markTimeAtSet
 * @reactProps {function} select
 * @reactProps {moment} max
 * @reactProps {moment} min
 * @reactProps {string} locale
 * @reactProps {object} time
 */
function PopUp(props) {
    const {
        dateFormat,
        markTimeAsSet,
        timeFormat,
        value,
        max,
        min,
        locale,
        time,
        isTimeSet,
        type,
    } = props

    const { t } = useTranslation()

    return (
        <div className="d-inline-flex justify-content-end">
            {Object.keys(value).map((input, i) => {
                const { hasDefaultTime, ...timeObj } = time[input]

                /* eslint-disable react/no-array-index-key */
                return (
                    <Calendar
                        key={i}
                        index={i}
                        values={value}
                        type={type}
                        time={timeObj}
                        markTimeAsSet={markTimeAsSet}
                        hasDefaultTime={hasDefaultTime || isTimeSet[input]}
                        inputName={input}
                        value={value[input]}
                        timeFormat={timeFormat}
                        select={props.select}
                        max={max}
                        min={min}
                        locale={locale}
                        dateFormat={dateFormat}
                        t={t}
                    />
                )
            })}
        </div>
    )
}

PopUp.propTypes = {
    value: PropTypes.instanceOf(moment),
    dateFormat: PropTypes.string,
    timeFormat: PropTypes.string,
    markTimeAsSet: PropTypes.func,
    select: PropTypes.func,
    max: PropTypes.instanceOf(moment),
    min: PropTypes.instanceOf(moment),
    locale: PropTypes.string,
    isTimeSet: PropTypes.any,
    type: PropTypes.any,
    time: PropTypes.shape({
        mins: PropTypes.number,
        hours: PropTypes.number,
    }),
}

export default onClickOutside(PopUp)
