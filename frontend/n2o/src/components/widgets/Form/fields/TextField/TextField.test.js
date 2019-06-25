import React from 'react';
import TextField from './TextField';

const setupTextField = propsOverride => {
  const props = {
    text: 'test',
  };

  return mount(<TextField {...props} {...propsOverride} />);
};

describe('Проверка компонента TextField', () => {
  it('Должен отрисовать текст', () => {
    const wrapper = setupTextField();
    expect(wrapper.find('.n2o-text-field').exists()).toEqual(true);
    expect(wrapper.text()).toEqual('test');
  });
  it('Не должен отрисоваться', () => {
    const wrapper = setupTextField({
      visible: false,
    });
    expect(wrapper.find('.n2o-text-field').exists()).toEqual(false);
  });
  it('Должен отформатировать текст', () => {
    const wrapper = setupTextField({
      text: '20.04.1934 00:00:00',
      format: 'date DD.MM.YYYY',
    });
    expect(wrapper.text()).toEqual('20.04.1934');
  });
});
