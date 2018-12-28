import { isNumber, isNil, toNumber } from 'lodash';

export function formatToFloat(val, precision) {
  if (isNil(val) || val === '') return null;
  const str = val
    .toString()
    .trim()
    .replace(',', '.');
  const end =
    str.indexOf('.', str.indexOf('.') + 1) === -1
      ? str.length
      : str.indexOf('.', str.indexOf('.') + 1);
  const formattedStr = str.slice(0, end);
  return toNumber(formattedStr).toFixed(precision);
}

export function getPrecision(step) {
  const stepArr = step
    .toString()
    .trim()
    .split('.');
  return stepArr.length === 1 ? 0 : stepArr[1].length;
}

export function isValid(val, min, max) {
  if (!min && !max) {
    return true;
  }
  return !isNil(val) && (toNumber(val) <= toNumber(max) || toNumber(val) >= toNumber(min));
}

export function matchesWhiteList(val) {
  return /^-?[0-9,\.]*$/.test(val);
}
