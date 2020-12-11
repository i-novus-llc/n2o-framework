import React from 'react';
import sinon from 'sinon';

import SearchBar from './SearchBar';

const delay = timeout => new Promise(res => setTimeout(res, timeout));
const setup = propsOverride => {
  const props = {
    className: 'my-search-bar',
  };

  return mount(<SearchBar {...props} {...propsOverride} />);
};
describe('<SearchBar />', () => {
  it('компонент должен отрисоваться', () => {
    const wrapper = setup();

    expect(wrapper.find('.n2o-search-bar').exists()).toBeTruthy();
    expect(wrapper.find('.my-search-bar').exists()).toBeTruthy();
  });

  it('должно проставиться initialValue', () => {
    const wrapper = setup({
      initialValue: 'some value',
    });

    expect(wrapper.find('input').props().value).toBe('some value');
  });

  it('должна измениться иконка', () => {
    const wrapper = setup({
      icon: 'fa fa-times',
    });

    expect(wrapper.find('.fa-times').exists()).toBeTruthy();
  });

  it('должна отрисоваться кнопка', () => {
    const button = {
      label: 'Искать',
      icon: 'fa fa-search',
      color: 'primary',
    };
    const wrapper = setup({
      button: button,
    });

    expect(wrapper.find('Button').exists()).toBeTruthy();
    expect(wrapper.find('Button').props().children[0]).toBe(button.label);
    expect(wrapper.find('i.fa-search').exists()).toBeTruthy();
    expect(wrapper.find('Button').props().color).toBe(button.color);
  });

  it('должен вызваться onSearch через 400ms', async () => {
    const onSearch = sinon.spy();
    const wrapper = setup({
      onSearch,
    });

    wrapper.find('input').simulate('change', {
      target: {
        value: 'Нина',
      },
    });

    expect(onSearch.calledOnce).toBeFalsy();
    await delay(400);
    expect(onSearch.calledOnce).toBeTruthy();
  });

  it('должен вызываться onSearch при trigger = ENTER', () => {
    const onSearch = sinon.spy();
    const wrapper = setup({
      trigger: 'ENTER',
      onSearch,
    });

    wrapper
      .find('input')
      .simulate('change', { target: { value: 'Александр' } });
    expect(onSearch.calledOnce).toBeFalsy();
    wrapper.find('input').simulate('keydown', {
      keyCode: 13,
    });
    expect(onSearch.calledOnce).toBeTruthy();
  });

  it('должен вызваться onSearch при клике на кнопку', () => {
    const onSearch = sinon.spy();
    const wrapper = setup({
      button: {
        label: 'Искать',
      },
      trigger: 'BUTTON',
      onSearch,
    });

    wrapper.find('input').simulate('change', {
      target: {
        value: 'Сергей',
      },
    });
    expect(onSearch.calledOnce).toBeFalsy();
    wrapper.find('Button').simulate('click');
    expect(onSearch.calledOnce).toBeTruthy();
  });
});
