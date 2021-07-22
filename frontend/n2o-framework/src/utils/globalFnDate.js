import moment from 'moment'

let formats = {
    dateFormat: 'yyyy-MM-dd',
    timeFormat: '00:00:00',
}

/**
 * Функция хелпер для соединения форматов
 * Берет формат из аргумента или глобальной переменной
 * @param overrideFormat
 * @returns {string}
 */
const getFormat = overrideFormat => `${(overrideFormat || formats).dateFormat}T${
    (overrideFormat || formats).timeFormat
}`

const fns = {
    /**
   * Возвращает текущий день
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
    now: newFormat => moment().format(getFormat(newFormat)),
    /**
   * Возвращает текущее время в UTC
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
    nowUTC: newFormat => moment.utc().format(getFormat(newFormat)),
    /**
   * Возвращает сегодняшний день
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
    today: newFormat => moment()
        .startOf('day')
        .format(getFormat(newFormat)),
    /**
   * Возвращает вчерашний день
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
    yesterday: newFormat => moment()
        .add(-1, 'd')
        .startOf('day')
        .format(getFormat(newFormat)),
    /**
   * Возвращает завтрешний день
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
    tomorrow: newFormat => moment()
        .add(1, 'd')
        .startOf('day')
        .format(getFormat(newFormat)),
    /**
   * Возвращает начало текущего дня
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
    beginDay: newFormat => moment()
        .startOf('day')
        .format(getFormat(newFormat)),
    /**
   * Возвращает конец текущего дня
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
    endDay: newFormat => moment()
        .endOf('day')
        .format(getFormat(newFormat)),

    /**
   * Возвращает начало текущей недели
   * @param newFormat
   * @returns {string}
   */
    beginWeek: newFormat => moment()
        .startOf('isoWeek')
        .format(getFormat(newFormat)),
    /**
   * Возвращает конец текущей недели
   * @param newFormat
   * @returns {string}
   */
    endWeek: newFormat => moment()
        .endOf('isoWeek')
        .format(getFormat(newFormat)),
    /**
   * Возвращает начало текущего месяца
   * @param newFormat
   * @returns {string}
   */
    beginMonth: newFormat => moment()
        .startOf('month')
        .format(getFormat(newFormat)),
    /**
   * Возвращает конец текущего месяца
   * @param newFormat
   * @returns {string}
   */
    endMonth: newFormat => moment()
        .endOf('month')
        .format(getFormat(newFormat)),
    /**
   * Возвращает начало текущего квартала
   * @param newFormat
   * @returns {string}
   */
    beginQuarter: newFormat => moment()
        .startOf('quarter')
        .format(getFormat(newFormat)),
    /**
   * Возвращает конец текущего месяца
   * @param newFormat
   * @returns {string}
   */
    endQuarter: newFormat => moment()
        .endOf('quarter')
        .format(getFormat(newFormat)),
    /**
   * Возвращает начало текущего года
   * @param newFormat
   * @returns {string}
   */
    beginYear: newFormat => moment()
        .startOf('year')
        .format(getFormat(newFormat)),
    /**
   * Возвращает конец текущего года
   * @param newFormat
   * @returns {string}
   */
    endYear: newFormat => moment()
        .endOf('year')
        .format(getFormat(newFormat)),
}

const dateFunctions = {
    addFormat: (overrideFormat) => {
        formats = overrideFormat
    },
    getFns: () => fns,
}

export default Object.freeze(dateFunctions)
