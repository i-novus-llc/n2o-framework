import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import { isEqual, forOwn, isEmpty, split } from 'lodash';
import InputMask from '../InputMask/InputMask';

const ReplaceableChar = {
  PREFIX: 'prefix',
  SUFFIX: 'suffix',
  THOUSANDS_SYMBOL: 'thousandsSeparatorSymbol',
  DECIMAL_SYMBOL: 'decimalSymbol'
};

/**
 * Компонент ввода денег
 * @reactProps {string} value - значение
 * @reactProps {string} className - класс компонента
 * @reactProps {object} presetConfig - настройка для ввода денег
 * @example
 * presetConfig: {
 *  prefix - вывод валюты в начале
 *  suffix - вывод валюты в конце
 *  includeThousandsSeparator - флаг включения разделителя тысяч
 *  thousandsSeparatorSymbol - разделитель тысяч
 *  allowDecimal - флаг разрешения float
 *  decimalSymbol - разделитель float
 *  decimalLimit - лимит float
 *  integerLimit - целочисленный лимит
 *  requireDecimal - флаг обязательного включения float
 *  allowNegative - флаг включения отрицательных чисел
 *  allowLeadingZeroes - флаг разрешения нулей вначале
 * }
 */
class InputMoney extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      value: props.value
    };

    this.onChange = this.onChange.bind(this);
    this.convertToMoney = this.convertToMoney.bind(this);
    this.convertToFloat = this.convertToFloat.bind(this);
    this.getInputMoneyProps = this.getInputMoneyProps.bind(this);
  }

  componentDidUpdate(prevProps) {
    if (!isEqual(prevProps.value, this.props.value)) {
      this.setState({ value: this.props.value });
    }
  }

  convertToMoney(value) {
    const { presetConfig } = this.props;

    if (!isEmpty(value) && presetConfig[ReplaceableChar.DECIMAL_SYMBOL] !== '.') {
      value = value.replace('.', presetConfig[ReplaceableChar.DECIMAL_SYMBOL]);
    }

    return value;
  }

  replaceSpecialSymbol(value, searchValue, replaceValue) {
    return value.replace(searchValue, replaceValue);
  }

  convertToFloat(value) {
    const { presetConfig } = this.props;
    let convertedValue = value.toString();
    forOwn(ReplaceableChar, char => {
      if (!isEmpty(presetConfig[char])) {
        const regExp = new RegExp(presetConfig[char], 'g');
        const replaceableValue = char === ReplaceableChar.DECIMAL_SYMBOL ? '.' : '';
        convertedValue = this.replaceSpecialSymbol(convertedValue, regExp, replaceableValue);
      }
    });

    const splitValue = split(convertedValue, '.');
    if (splitValue.length === 2 && isEmpty(splitValue[1])) {
      return split(convertedValue, '.')[0] + '.' + '0';
    }

    return convertedValue;
  }

  onChange(value) {
    const { onChange } = this.props;
    const convertedValue = this.convertToFloat(value);
    onChange && onChange(convertedValue);
    this.setState({ value: convertedValue });
  }

  getInputMoneyProps() {
    const { value, className } = this.props;

    return {
      ...this.props,
      value: this.convertToMoney(value),
      onChange: this.onChange,
      className: cn('n2o-input-money', className)
    };
  }

  render() {
    return <InputMask {...this.getInputMoneyProps()} />;
  }
}

InputMoney.propTypes = {
  value: PropTypes.string,
  presetConfig: PropTypes.object,
  className: PropTypes.string
};

InputMoney.defaultProps = {
  value: '',
  presetConfig: {
    prefix: '',
    suffix: ' руб.',
    includeThousandsSeparator: true,
    thousandsSeparatorSymbol: ' ',
    allowDecimal: true,
    decimalSymbol: ',',
    decimalLimit: null,
    integerLimit: null,
    requireDecimal: false,
    allowNegative: false,
    allowLeadingZeroes: false
  }
};

export default InputMoney;
