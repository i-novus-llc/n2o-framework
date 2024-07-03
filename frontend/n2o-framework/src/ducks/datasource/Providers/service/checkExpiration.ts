import moment, { Duration } from 'moment/moment'

enum UNIT {
    DAYS = 'd',
    HOURS = 'h',
    MINUTES = 'm',
    SECONDS = 's'
}

function increaseDuration(duration: Duration, time: string, unit: UNIT) {
    const [value] = time.split(unit)

    duration.add(moment.duration(value, unit))
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

    const date = moment(timestamp)
    const duration = moment.duration()

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

    date.add(duration)

    return moment().isAfter(date)
}
