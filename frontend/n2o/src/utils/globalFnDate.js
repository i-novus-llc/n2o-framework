import moment from 'moment';
import { each } from 'lodash';

let formats = {
  dateFormat: 'DD.MM.YYYY',
  timeFormat: 'hh.mm'
};

const getFormat = () => `${formats.dateFormat} ${formats.timeFormat}`;

const fns = {
  /**
   * Возвращает текущий день
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
  now: newFormat => moment().format(newFormat || getFormat()),
  /**
   * Возвращает текущее время в UTC
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
  nowUTC: newFormat => moment.utc().format(newFormat || getFormat()),

  /**
   * Возвращает вчерашний день
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
  yesterday: newFormat =>
    moment()
      .add(-1, 'd')
      .format(newFormat || getFormat()),
  /**
   * Возвращает завтрешний день
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
  tomorrow: newFormat =>
    moment()
      .add(1, 'd')
      .format(newFormat || getFormat()),
  /**
   * Возвращает начало текущего дня
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
  beginDay: newFormat =>
    moment()
      .startOf('day')
      .format(newFormat || getFormat()),
  /**
   * Возвращает конец текущего дня
   * @param newFormat - кастомный формат даты
   * @returns {string}
   */
  endDay: newFormat =>
    moment()
      .endOf('day')
      .format(newFormat || getFormat()),

  /**
   * Возвращает начало текущей недели
   * @param newFormat
   * @returns {string}
   */
  beginWeek: newFormat =>
    moment()
      .startOf('isoWeek')
      .format(newFormat || getFormat()),
  /**
   * Возвращает конец текущей недели
   * @param newFormat
   * @returns {string}
   */
  endWeek: newFormat =>
    moment()
      .endOf('isoWeek')
      .format(newFormat || getFormat()),
  /**
   * Возвращает начало текущего месяца
   * @param newFormat
   * @returns {string}
   */
  beginMonth: newFormat =>
    moment()
      .startOf('month')
      .format(newFormat || getFormat()),
  /**
   * Возвращает конец текущего месяца
   * @param newFormat
   * @returns {string}
   */
  endMonth: newFormat =>
    moment()
      .endOf('month')
      .format(newFormat || getFormat()),
  /**
   * Возвращает начало текущего квартала
   * @param newFormat
   * @returns {string}
   */
  beginQuarter: newFormat =>
    moment()
      .startOf('quarter')
      .format(newFormat || getFormat()),
  /**
   * Возвращает конец текущего месяца
   * @param newFormat
   * @returns {string}
   */
  endQuarter: newFormat =>
    moment()
      .endOf('quarter')
      .format(newFormat || getFormat()),
  /**
   * Возвращает начало текущего года
   * @param newFormat
   * @returns {string}
   */
  beginYear: newFormat =>
    moment()
      .startOf('year')
      .format(newFormat || getFormat()),
  /**
   * Возвращает конец текущего года
   * @param newFormat
   * @returns {string}
   */
  endYear: newFormat =>
    moment()
      .endOf('year')
      .format(newFormat || getFormat())
};

const dateFunctions = {
  addFormat: overrideFormat => {
    formats = overrideFormat;
  },
  getFns: () => fns
};

export default Object.freeze(dateFunctions);
