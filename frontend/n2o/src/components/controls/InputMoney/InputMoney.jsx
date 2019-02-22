import React from 'react';
import PropTypes from 'prop-types';
import { isEqual, forOwn, map, reverse } from 'lodash';
import InputMask from '../InputMask/InputMask';

const ReplaceableChar = {
  PREFIX: 'prefix',
  SUFFIX: 'suffix',
  THOUSANDS_SYMBOL: 'thousandsSeparatorSymbol',
  DECIMAL_SYMBOL: 'decimalSymbol'
};

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
      // this.setState({ value: this.props.value });
    }
  }

  convertToMoney(value) {
    const { presetConfig } = this.props;
    let newValue = value;
    if (presetConfig[ReplaceableChar.THOUSANDS_SYMBOL]) {
      let intValue = newValue.split(presetConfig[ReplaceableChar.DECIMAL_SYMBOL])[0];
      console.log(intValue.split(''));
      intValue = intValue.split('').reverse();
      map(intValue, (letter, index) => {
        if (index % 3 === 0) intValue.splice(index + 1, 0, ` `);
      });
      console.log(intValue);
      console.log(intValue.reverse().join(''));
    }
    let splittedValue = value.toString().split('');
    if (presetConfig[ReplaceableChar.PREFIX]) {
      splittedValue.unshift(presetConfig[ReplaceableChar.PREFIX]);
    }
    if (presetConfig[ReplaceableChar.SUFFIX]) {
      splittedValue.push(presetConfig[ReplaceableChar.SUFFIX]);
    }
    let stingValue = splittedValue.join('');
    // return stingValue;
  }

  convertToFloat(value) {
    const { presetConfig } = this.props;
    let convertedValue = value.toString();
    forOwn(ReplaceableChar, char => {
      if (presetConfig[char] && char !== ReplaceableChar.DECIMAL_SYMBOL) {
        const regExp = new RegExp(presetConfig[char], 'g');
        convertedValue = convertedValue.replace(regExp, '');
      }
    });
    if (presetConfig[ReplaceableChar.DECIMAL_SYMBOL] === ',') {
      convertedValue = convertedValue.replace(',', '.');
    }
    convertedValue = convertedValue.replace(/ /g, '');
    return convertedValue;
  }

  onChange(value) {
    const { onChange } = this.props;
    const convertedValue = this.convertToFloat(value);
    onChange && onChange(convertedValue);
    this.setState({ value: convertedValue });
  }

  getInputMoneyProps() {
    const { value } = this.props;

    return {
      ...this.props,
      value: this.convertToMoney(value),
      onChange: this.onChange
    };
  }

  render() {
    return <InputMask {...this.getInputMoneyProps()} />;
  }
}

InputMoney.propTypes = {
  value: PropTypes.string,
  presetConfig: PropTypes.object
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
