import React from 'react';
import { mount, shallow } from 'enzyme';
import ErrorPage from './ErrorPage';

const setup = propsOverride => {
  const props = {};

  return mount(<ErrorPage {...props} {...propsOverride} />);
};

describe('<ErrorPage />', () => {
  it('компонент должен отрисоваться', () => {
    const wrapper = setup();

    expect(wrapper.find('.container').exists()).toBeTruthy();
  });

  it('параметры должны проставиться', () => {
    const wrapper = setup({
      status: 404,
      error: 'Not Found!',
    });

    expect(wrapper.find('h1').text()).toBe('404');
    expect(wrapper.find('.container span').text()).toBe('Not Found!');
  });
});
