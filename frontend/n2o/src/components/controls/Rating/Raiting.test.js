import React from 'react';
import sinon from 'sinon';
import Rating from './Rating';

describe('<Rating />', () => {
  it('Создание компонента ', () => {
    const wrapper = mount(<Rating />);
    expect(wrapper.exists()).toBeTruthy();
  });

  it('проверка max', () => {
    const wrapper = mount(<Rating max={10} />);
    expect(wrapper.find('label').length).toBe(11);
  });
  it('проверка rating', () => {
    const wrapper = mount(<Rating rating={5} max={10} />);
    expect(wrapper.find('input[checked=true]').length).toBe(1);
  });
  it('проверка half', () => {
    const wrapper = mount(<Rating rating={5} max={10} half={true} />);
    expect(wrapper.find('label').length).toBe(21);
    expect(wrapper.find('.rating__label--half').length).toBe(10);
  });
});
