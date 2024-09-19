import React from 'react'
import { Dayjs } from 'dayjs'
import onClickOutside from 'react-onclickoutside'
import { useTranslation } from 'react-i18next'

import { Calendar } from './Calendar'
import { Time, DefaultTime } from './types'

type PopUpProps = {
    dateFormat: string,
    isTimeSet: Record<string, boolean | undefined>,
    locale: 'en' | 'ru',
    markTimeAsSet(str: string): void,
    max?: Dayjs | Date | string,
    min?: Dayjs | Date | string,
    select(day: Dayjs | null, inputName: string, close?: boolean): void,
    time: DefaultTime,
    timeFormat?: string,
    type?: string,
    value: Record<string, Dayjs | null>
}

export const PopUpComponent = ({
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

PopUpComponent.prototype = {}

export const PopUp = onClickOutside(PopUpComponent)
