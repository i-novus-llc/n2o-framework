import React from 'react';
import Spinner from './Spinner';

const setup = props => {
  return mount(<Spinner {...props} />);
};

describe('Тесты Spinner', () => {
  it('Отрисовывается spinner cover', () => {
    const wrapper = setup({ endTimeout: false, loading: true, type: 'cover' });
    expect(wrapper.find('.spinner-cover').exists()).toEqual(true);
  });
  it('Отрисовывается spinner background', () => {
    const wrapper = setup({
      endTimeout: false,
      loading: true,
      type: 'cover',
      transparent: false,
    });
    expect(wrapper.find('.spinner-background').exists()).toEqual(true);
  });
  it('Отрисовывается children', () => {
    const wrapper = setup({
      endTimeout: false,
      loading: true,
      type: 'cover',
      children: <div className="test" />,
    });
    expect(wrapper.find('.test').exists()).toBe(true);
  });
});
