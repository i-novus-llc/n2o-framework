import React from 'react';
import InputMoney from '../controls/InputMoney/InputMoney';
import InputMask from './InputMask/InputMask';
import InputNumber from './InputNumber/InputNumber';
import InputText from './InputText/InputText';
import TextArea from './TextArea/TextArea';

const setup = propsOverride => {
  return mount(<InputMoney {...propsOverride} />);
};

describe('Проверка компонента-обертки withRightPlaceholder', () => {
  it('InputMoney', () => {
    const wrapper = setup({ rightPlaceholder: 'шт' });
    expect(wrapper.find('.n2o-control-container-placeholder').exists()).toEqual(
      true
    );
  });
  it('InputMask', () => {
    const wrapper = setup({ allowDecimal: true });
    expect(wrapper.instance().convertToMoney('123.33')).toEqual('123,33');
  });
  it('InputNumber', () => {
    const wrapper = setup({
      prefix: '# ',
      includeThousandsSeparator: true,
      thousandsSeparatorSymbol: ' ',
      decimalSymbol: ',',
    });
    expect(wrapper.instance().convertToFloat('# 1 234,55')).toEqual('1234.55');
  });
});
