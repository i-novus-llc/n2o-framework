import React from 'react'
import { useTranslation } from 'react-i18next'

import { Calendar } from './Calendar'
import { type Time, type PopUpProps } from './types'

export const PopUp = ({
    dateFormat,
    markTimeAsSet,
    timeFormat,
    value,
    max,
    min,
    locale,
    select,
    time,
    isTimeSet,
    type,
}: PopUpProps) => {
    const { t } = useTranslation()

    return (
        <div className="d-inline-flex justify-content-end">
            {Object.keys(value).map((input, i) => {
                const { hasDefaultTime, ...timeObj } = time[input]

                return (
                    <Calendar
                        /* eslint-disable react/no-array-index-key */
                        key={i}
                        index={i}
                        values={value}
                        type={type}
                        time={timeObj as Time}
                        markTimeAsSet={markTimeAsSet}
                        hasDefaultTime={hasDefaultTime || isTimeSet[input]}
                        inputName={input}
                        value={value[input]}
                        timeFormat={timeFormat}
                        select={select}
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
