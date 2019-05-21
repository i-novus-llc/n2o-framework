import React from 'react';
import {
  omit,
  pick,
  isEmpty,
  forEach,
  isString,
  isArray,
  isNil,
  map,
} from 'lodash';

const parseToFloat = value =>
  !isNil(parseFloat(value)) ? parseFloat(value) : value;

const parseToInt = value => (!isNil(parseInt(value)) ? parseInt(value) : value);

const convertStrToFloatOrInt = value => {
  if (value.includes('.')) {
    return parseToFloat(value);
  }
  return parseToInt(value);
};

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
        resultConverted[key] = map(convertStrToFloatOrInt);
      } else {
        resultConverted[key] = value;
      }
    });
  }

  return (
    <WrapperComponent {...omit(rest, convertProps)} {...resultConverted} />
  );
};
