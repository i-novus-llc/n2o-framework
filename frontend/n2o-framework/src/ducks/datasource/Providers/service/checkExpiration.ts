import dayjs from 'dayjs'
import duration, { Duration } from 'dayjs/plugin/duration'

dayjs.extend(duration)

enum UNIT {
    DAYS = 'd',
    HOURS = 'h',
    MINUTES = 'm',
    SECONDS = 's',
}

function increaseDuration(duration: Duration, time: string, unit: UNIT) {
    const [value] = time.split(unit)

    const numValue = parseInt(value, 10)

    switch (unit) {
        case UNIT.DAYS:
            duration.add(dayjs.duration(numValue, 'day'))

            break
        case UNIT.HOURS:
            duration.add(dayjs.duration(numValue, 'hour'))

            break
        case UNIT.MINUTES:
            duration.add(dayjs.duration(numValue, 'minute'))

            break
        case UNIT.SECONDS:
            duration.add(dayjs.duration(numValue, 'second'))

            break
        default:
            throw new Error(`Unsupported unit: ${unit}`)
    }
}

/**
 *
 * @param timestamp
 * @example 'YYYY-MM-DDThh:mm:ss'
 * @param cacheExpires
 * @example '1d 5h 30m'
 * @returns {boolean}
 */
export function checkExpiration(timestamp: string, cacheExpires: string): boolean {
    if (!cacheExpires) { return false }

    const date = dayjs(timestamp)
    const duration = dayjs.duration(0, 'second')

    for (const time of cacheExpires.split(' ')) {
        if (time.includes(UNIT.DAYS)) {
            increaseDuration(duration, time, UNIT.DAYS)
        } else if (time.includes(UNIT.HOURS)) {
            increaseDuration(duration, time, UNIT.HOURS)
        } else if (time.includes(UNIT.MINUTES)) {
            increaseDuration(duration, time, UNIT.MINUTES)
        } else if (time.includes(UNIT.SECONDS)) {
            increaseDuration(duration, time, UNIT.SECONDS)
        }
    }

    const expirationDate = date.add(duration.asMilliseconds(), 'millisecond')

    return dayjs().isAfter(expirationDate)
}
