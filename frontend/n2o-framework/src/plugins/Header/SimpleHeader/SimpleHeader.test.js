import React from 'react';
import SimpleHeader from './SimpleHeader';

const setup = props => {
  return mount(<SimpleHeader {...props} />);
};

describe('Тесты SimpleHeader', () => {
  it('отрисовка search', () => {
    const wrapper = setup({ search: true });
    expect(wrapper.find('.fa-search').exists()).toBe(true);
  });
  it('отрисовка fixed', () => {
    const wrapper = setup({ fixed: true });
    expect(wrapper.find('.navbar-container-fixed').exists()).toBe(true);
  });
});
