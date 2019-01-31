import moment from 'moment';
import { each } from 'lodash';

const TYPES = {
  FORMAT: 'DD.MM.YYYY HH:mm',
  FORMAT00: 'DD.MM.YYYY 00:00',
  FORMAT59: 'DD.MM.YYYY 23:59'
};

const dateFunctions = {
  /**
   * Возвращает текущий день в формате DD.MM.YYYY HH:mm
   * @returns {string}
   */
  now: () => moment().format(TYPES.FORMAT),
  /**
   * Возвращает текущее время в UTC формате
   * @returns {string}
   */
  nowUTC: () => moment.utc().format(TYPES.FORMAT),
  /**
   * Возвращает день в формате DD.MM.YYYY 00:00
   * @returns {string}
   */
  today: () => moment().format(TYPES.FORMAT00),

  /**
   * Возвращает вчерашний день в формате DD.MM.YYYY 00:00
   * @returns {string}
   */
  yesterday: () =>
    moment()
      .add('d', -1)
      .format(TYPES.FORMAT00),
  /**
   * Возвращает затрешний день в формате DD.MM.YYYY 00:00
   * @returns {string}
   */
  tomorrow: () =>
    moment()
      .add('d', 1)
      .format(TYPES.FORMAT00),
  /**
   * Возвращает начало текущего дня в формате DD.MM.YYYY 00:00
   * @returns {string}
   */
  beginDay: () => moment().format(TYPES.FORMAT00),
  /**
   * Возвращает конец текущего дня в формате DD.MM.YYYY 23:59
   * @returns {string}
   */
  endDay: () => moment().format(TYPES.FORMAT59),

  beginWeek: () =>
    moment()
      .startOf('isoWeek')
      .format(TYPES.FORMAT00),
  endWeek: () =>
    moment()
      .endOf('isoWeek')
      .format(TYPES.FORMAT00),
  beginMonth: () =>
    moment()
      .startOf('month')
      .format(TYPES.FORMAT00),
  endMonth: () =>
    moment()
      .endOf('month')
      .format(TYPES.FORMAT00),
  beginQuarter: () =>
    moment()
      .startOf('quarter')
      .format(TYPES.FORMAT00),
  endQuarter: () =>
    moment()
      .endOf('quarter')
      .format(TYPES.FORMAT00),
  beginYear: () =>
    moment()
      .startOf('year')
      .format(TYPES.FORMAT00),
  endYear: () =>
    moment()
      .endOf('year')
      .format(TYPES.FORMAT00)
};

each(dateFunctions, (fn, key) => {
  window[key] = fn;
});

export default dateFunctions;
