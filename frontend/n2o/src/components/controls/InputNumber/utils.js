import { isNumber } from 'lodash';

export function formatToFloat(val, precision) {
  if (!val) return null;
  const str = val
    .toString()
    .trim()
    .replace(',', '.');
  const end =
    str.indexOf('.', str.indexOf('.') + 1) === -1
      ? str.length
      : str.indexOf('.', str.indexOf('.') + 1);
  const formattedStr = str.slice(0, end);
  return Number(formattedStr).toFixed(precision);
}

export function getPrecision(step) {
  const stepArr = step
    .toString()
    .trim()
    .split('.');
  return stepArr.length === 1 ? 0 : stepArr[1].length;
}

export function isValid(val, min, max) {
  return !isNaN(val) && (!isNumber(max) || val <= max) && (!isNumber(min) || val >= min);
}

export function matchesWhiteList(val) {
  return /^-?[0-9,\.]*$/.test(val);
}
