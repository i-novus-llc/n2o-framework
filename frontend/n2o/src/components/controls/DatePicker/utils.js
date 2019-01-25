import moment from 'moment';
import { keys, maxBy } from 'lodash';

/**
 * Дата (date) была после конца месяца другой даты(displayedMonth) или нет
 * @param date
 * @param displayedMonth
 */
export function isDateFromNextMonth(date, displayedMonth) {
  return (
    date.year() > displayedMonth.year() ||
    (date.year() === displayedMonth.year() && date.month() > displayedMonth.month())
  );
}

/**
 * Дата (date) была до начала месяца другой даты(displayedMonth) или нет
 * @param date
 * @param displayedMonth
 */
export function isDateFromPrevMonth(date, displayedMonth) {
  return (
    date.year() < displayedMonth.year() ||
    (date.year() === displayedMonth.year() && date.month() < displayedMonth.month())
  );
}

/**
 * Возвращает массив из недель(каждая неделя - массив из дней)
 * @param firstDay
 */
export function weeks(firstDay) {
  const lastDay = firstDay.clone().add(35, 'days');
  const day = firstDay.clone();
  let weeks = [];
  while (day <= lastDay) {
    let week = [];
    for (let i = 0; i < 7; i++) {
      week.push(day.clone());
      if (i === 6) {
        weeks.push(week);
      }
      day.add(1, 'days');
    }
  }
  return weeks;
}

/**
 * добавить часы и минуты к дате
 * @param date
 * @param hours
 * @param mins
 */
export function addTime(date, hours, mins, secs) {
  return date
    .clone()
    .add(hours, 'h')
    .add(mins, 'm')
    .add(secs, 's');
}

/**
 * добавть локаль
 * @param date
 * @param locale
 */
export function withLocale(date, locale) {
  return date.clone().locale(locale);
}

/**
 * преобразовать дату к moment-объекту
 * @param value
 * @param dateFormat
 */
export function parseDate(value, dateFormat) {
  if (value instanceof Date) {
    value = moment(value);
  } else if (typeof value === 'string') {
    value = moment(value, dateFormat);
    if (!value.isValid()) {
      console.log('Invalid date');
    }
  }
  return value;
}

/**
 * Привести по пропсам к нужному формату
 * @param val
 * @param defaultTime
 * @param dateFormat
 * @param locale
 * @param defaultName
 */
export function mapToValue(val, defaultTime, dateFormat, locale, defaultName) {
  let res = {};
  if (Array.isArray(val)) {
    val.map(input => {
      if (!input.value) {
        res[input.name] = null;
      } else {
        const value = addTime(
          withLocale(parseDate(input.value, dateFormat), locale).startOf('day'),
          defaultTime[input.name],
          defaultTime[input.name].mins,
          defaultTime[input.name].seconds
        );
        res[input.name] = value;
      }
    });
    return res;
  }
  if (!val) return { [defaultName]: null };
  const value = addTime(
    withLocale(parseDate(val, dateFormat), locale).startOf('day'),
    defaultTime[defaultName].hours,
    defaultTime[defaultName].mins,
    defaultTime[defaultName].seconds
  );
  res[defaultName] = value;
  return res;
}

/**
 * Установаить дефолтное время
 * @param val
 * @param defaultTime
 * @param defaultName
 */
export function mapToDefaultTime(val, defaultTime, defaultName, timeFormat, format) {
  if (Array.isArray(val)) {
    let res = {};
    val.map(input => {
      res[input.name] = {
        hours:
          moment(input.value, format).hour() ||
          moment(input.defaultTime || '00:00', timeFormat).hour() ||
          0,
        mins:
          moment(input.value, format).minute() ||
          moment(input.defaultTime || '00:00', timeFormat).minute() ||
          0,
        seconds:
          moment(input.value, format).second() ||
          moment(input.defaultTime || '00:00', timeFormat).second() ||
          0,
        hasDefaultTime: false
      };
      if (res[input.name].hours || res[input.name].mins || res[input.name].seconds || timeFormat) {
        res[input.name].hasDefaultTime = true;
      }
    });

    return res;
  }

  if (val) {
    return {
      [defaultName]: {
        hours: moment(val, format).hour(),
        mins: moment(val, format).minute(),
        seconds: moment(val, format).second(),
        hasDefaultTime: true
      }
    };
  }

  let ress = {
    [defaultName]: {
      hours: moment(defaultTime, timeFormat).hour(),
      mins: moment(defaultTime, timeFormat).minute(),
      seconds: moment(defaultTime, timeFormat).second(),
      hasDefaultTime: false
    }
  };

  if (ress[defaultName].hours || ress[defaultName].mins || ress[defaultName].seconds) {
    ress[defaultName].hasDefaultTime = true;
  }

  return ress;
}

/**
 * узнать высоту DOM-элемента
 * @param el
 */
export function getAbsoluteHeight(el) {
  // Get the DOM Node if you pass in a string
  el = typeof el === 'string' ? document.querySelector(el) : el;

  let styles = window.getComputedStyle(el);
  let margin = parseFloat(styles['marginTop']) + parseFloat(styles['marginBottom']);

  return Math.ceil(el.offsetHeight + margin);
}

/**
 * узнать ширину DOM-элемента
 * @param el
 */
export function getAbsoluteWidth(el) {
  // Get the DOM Node if you pass in a string
  el = typeof el === 'string' ? document.querySelector(el) : el;

  let styles = window.getComputedStyle(el);
  let margin = parseFloat(styles['marginLeft']) + parseFloat(styles['marginRight']);

  return Math.ceil(el.offsetWidth + margin);
}

export function buildDateFormat(dateFormat, timeFormat, divider) {
  return (
    (dateFormat ? dateFormat : '') +
    (dateFormat && timeFormat ? divider : '') +
    (timeFormat ? timeFormat : '')
  );
}

/**
 * Вычисляем наибольшее свободное пространство рядом с элементом
 * Возвращаем одно из вариантов [left, top, right, bottom]
 * @param input - input dateInterval
 * @param popUp - popUp dateInterval
 * @param window - глобальный обьект window
 * @returns {*}
 */
export function calculateMaxFreeSpace(input, popUp, window) {
  const inputPosition = input.getBoundingClientRect();
  const popUpPosition = popUp.getBoundingClientRect();

  /* placements:
                        ^
                        |
                       top
                        |
  <------left----| DateInputGroup |-------right------->
                        |
                        |
                      bottom
                        |

   */

  const placements = {
    left: inputPosition.left,
    top: inputPosition.top,
    right: window.innerWidth - inputPosition.right,
    bottom: window.innerHeight - inputPosition.bottom
  };

  // Не даем открыться вниз или вверх если попап выходит за рамки экрана.
  // Нужно из-за того что попап прибит при placement top и bottom к правому краю.
  if (inputPosition.width + inputPosition.left < popUpPosition.width) {
    placements.bottom = -1;
    placements.top = -1;
  }

  return maxBy(keys(placements), o => placements[o]);
}
