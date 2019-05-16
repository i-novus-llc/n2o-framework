import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import {
  isEqual,
  forOwn,
  isEmpty,
  split,
  replace,
  includes,
  findIndex,
} from 'lodash';
import InputMask from '../InputMask/InputMask';

const ReplaceableChar = {
  PREFIX: 'prefix',
  SUFFIX: 'suffix',
  THOUSANDS_SYMBOL: 'thousandsSeparatorSymbol',
  DECIMAL_SYMBOL: 'decimalSymbol',
};

/**
 * Компонент ввода денег
 * @reactProps {string} value - значение
 * @reactProps {string} className - класс компонента
 * @reactProps {string} prefix - вывод валюты в начале
 * @reactProps {string}  suffix - вывод валюты в конце
 * @reactProps {boolean}   includeThousandsSeparator - флаг включения разделителя тысяч
 * @reactProps {string}   thousandsSeparatorSymbol - разделитель тысяч
 * @reactProps {boolean}  allowDecimal - флаг разрешения float
 * @reactProps {string}  decimalSymbol - разделитель float
 * @reactProps {number}  decimalLimit - лимит float
 * @reactProps {number}  integerLimit - целочисленный лимит
 * @reactProps {boolean}  allowNegative - флаг включения отрицательных чисел
 * @reactProps {boolean}  allowLeadingZeroes - флаг разрешения нулей вначале
 * @example
 */
class InputMoney extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      value: props.value,
    };

    this.onBlur = this.onBlur.bind(this);
    this.onChange = this.onChange.bind(this);
    this.onBlur = this.onBlur.bind(this);
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
    const { allowDecimal } = this.props;
    if (value !== '') {
      value = replace(value, '.', this.props[ReplaceableChar.DECIMAL_SYMBOL]);
    }

    const splitBySymbol = split(
      value,
      this.props[ReplaceableChar.DECIMAL_SYMBOL]
    );

    if (!allowDecimal) {
      value = splitBySymbol[0];
    } else if (splitBySymbol.length === 2 && splitBySymbol[1].length === 1) {
      value =
        splitBySymbol[0] +
        this.props[ReplaceableChar.DECIMAL_SYMBOL] +
        splitBySymbol[1] +
        '0';
    }
    return value;
  }

  onBlur(value) {
    const { onBlur } = this.props;
    const convertedValue = this.convertToFloat(value);
    onBlur && onBlur(convertedValue);
    this.setState({ value: convertedValue });
  }

  replaceSpecialSymbol(value, searchValue, replaceValue) {
    return value.replace(searchValue, replaceValue);
  }

  convertToFloat(value) {
    const { allowDecimal } = this.props;
    let convertedValue = value.toString();
    forOwn(ReplaceableChar, char => {
      if (!isEmpty(this.props[char])) {
        const pattern = this.props[char].replace(
          /[-\/\\^$*+?.()|[\]{}]/g,
          '\\$&'
        );
        const regExp = new RegExp(pattern, 'g');
        const replaceableValue =
          char === ReplaceableChar.DECIMAL_SYMBOL ? '.' : '';
        convertedValue = this.replaceSpecialSymbol(
          convertedValue,
          regExp,
          replaceableValue
        );
      }
    });

    if (
      allowDecimal &&
      includes(this.state.value, '.') &&
      !includes(value, this.props[ReplaceableChar.DECIMAL_SYMBOL])
    ) {
      convertedValue = convertedValue.substring(0, convertedValue.length - 3);
    }
    this.setState({ value: convertedValue });
    return convertedValue;
  }

  onChange(value) {
    const { onChange } = this.props;
    const convertedValue = this.convertToFloat(value);
    onChange && onChange(convertedValue);
    this.setState({ value: convertedValue });
  }

  onBlur(value) {
    const { onBlur } = this.props;
    const convertedValue = this.convertToFloat(value);
    onBlur && onBlur(convertedValue);
    this.setState({ value: convertedValue });
  }

  getInputMoneyProps() {
    const {
      value,
      className,
      suffix,
      prefix,
      includeThousandsSeparator,
      thousandsSeparatorSymbol,
      allowDecimal,
      decimalSymbol,
      decimalLimit,
      integerLimit,
      allowNegative,
      allowLeadingZeroes,
    } = this.props;
    return {
      ...this.props,
      preset: 'money',
      value: this.convertToMoney(value || this.state.value),
      onChange: this.onChange,
      onBlur: this.onBlur,
      className: cn('n2o-input-money', className),
      presetConfig: {
        suffix,
        prefix,
        includeThousandsSeparator,
        thousandsSeparatorSymbol,
        allowDecimal,
        decimalSymbol,
        decimalLimit,
        integerLimit,
        requireDecimal: false,
        allowNegative,
        allowLeadingZeroes,
      },
    };
  }

  render() {
    return <InputMask {...this.getInputMoneyProps()} />;
  }
}

InputMoney.propTypes = {
  value: PropTypes.string,
  className: PropTypes.string,
  prefix: PropTypes.string,
  suffix: PropTypes.string,
  includeThousandsSeparator: PropTypes.bool,
  thousandsSeparatorSymbol: PropTypes.string,
  allowDecimal: PropTypes.bool,
  decimalSymbol: PropTypes.string,
  decimalLimit: PropTypes.number,
  integerLimit: PropTypes.any,
  allowNegative: PropTypes.bool,
  allowLeadingZeroes: PropTypes.bool,
};

InputMoney.defaultProps = {
  value: '',
  prefix: '',
  suffix: ' руб.',
  includeThousandsSeparator: true,
  thousandsSeparatorSymbol: ' ',
  allowDecimal: true,
  decimalSymbol: ',',
  decimalLimit: 2,
  integerLimit: null,
  allowNegative: false,
  allowLeadingZeroes: false,
  guide: false,
};

export default InputMoney;
