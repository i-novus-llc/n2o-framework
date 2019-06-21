import moment from 'moment';
import numeral from 'numeral';
import { split, trim, join, slice, map, isNil, isString } from 'lodash';

const typesFunctions = {
  date: ({ data, format }) => moment(data).format(format),
  password: ({ data }) => join(map(data, () => '*'), ''),
  number: ({ data, format }) => {
    const number = isString(data) ? Number(data) : data;
    return numeral(number).format(format);
  },
  dateFromNow: ({ format }) => moment().format(format),
};

/**
 * Преобразует строку по формату
 * Примеры форматов:
 * format="date DD.MM.YYYY HH:mm"
 * format="date DD.MM.YYYY"
 * format="dateFromNow"
 * format="password"
 * format="number 0,0.00"
 * format="number 0,0.00[000]"
 * @param data - исходная строка
 * @param typeAndformat - строка с типом данных и форматом
 * @returns {*}
 */
function parseFormatter(data, typeAndformat = false) {
  if (isNil(data) || !typeAndformat) return data;

  const typeAndFormat = split(trim(typeAndformat), ' ');

  if (!typeAndFormat.length) return null;

  const type = typeAndFormat[0];
  const format = join(slice(typeAndFormat, 1), ' ');

  return typesFunctions[type]({ data, format });
}

export default parseFormatter;
