import React, { useEffect, useState } from 'react'
import moment from 'moment/moment'

/**
 * Компонент Clock
 * @reactProps {function} onClick - событие клика
 */

type ClockProps = {
    onClick(): void,
}

export const Clock = ({ onClick }: ClockProps) => {
    const [time, setTime] = useState(moment())

    useEffect(() => {
        const interval = setInterval(() => {
            setTime(moment())
        }, 1000)

        return () => clearInterval(interval)
    }, [])

    return (
        <div className="n2o-calendar-clock" onClick={onClick}>
            {time.format('H:mm:ss')}
        </div>
    )
}
