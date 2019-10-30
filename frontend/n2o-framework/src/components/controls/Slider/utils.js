import React from 'react';
import {
  omit,
  pick,
  isEmpty,
  forEach,
  isString,
  isArray,
  isNaN,
  map,
  merge,
} from 'lodash';

export const parseToFloat = value =>
  !isNaN(parseFloat(value)) ? parseFloat(value) : undefined;

export const parseToInt = value =>
  !isNaN(parseInt(value)) ? parseInt(value) : undefined;

const convertStrToFloatOrInt = value => {
  if (isString(value)) {
    if (value.includes('.')) {
      return parseToFloat(value);
    }
    return parseToInt(value);
  }
  return value;
};
/**
 * HOC для преобразования заданных пропсов из строки в число
 * @param convertProps - список свойств
 * @return {function}
 */
export const stringConverter = (convertProps = []) => WrapperComponent => ({
  stringMode,
  ...rest
}) => {
  const convertPropsObj = pick(rest, stringMode ? convertProps : []);

  const resultConverted = {};

  if (!isEmpty(convertPropsObj)) {
    forEach(convertPropsObj, (value, key) => {
      if (isString(value)) {
        resultConverted[key] = convertStrToFloatOrInt(value);
      } else if (isArray(value)) {
        resultConverted[key] = map(value, convertStrToFloatOrInt);
      } else {
        resultConverted[key] = value;
      }
    });
  }

  return (
    <WrapperComponent {...omit(rest, convertProps)} {...resultConverted} />
  );
};

/**
 * Кастомный стиль если vertical=true
 * @param vertical - установлен ли вертикальный режим
 * @param style - стили для мерджа
 * @return {*}
 */
export const prepareStyle = (vertical = false, style = {}) => {
  const fromVerticalStyle = {
    float: 'left',
    height: 400,
    marginLeft: 50,
  };

  if (vertical) {
    return merge(fromVerticalStyle, style);
  }
  return style;
};
