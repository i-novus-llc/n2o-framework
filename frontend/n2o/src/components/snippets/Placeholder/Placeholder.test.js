import React from 'react';
import Placeholder from './Placeholder';

const setup = props => {
  return mount(<Placeholder {...props} />);
};

describe('Тесты Placeholder', () => {
  it('componentDidUpdate', () => {
    const wrapper = setup({ loading: true });
    expect(wrapper.instance().stopRender).toBe(false);
    wrapper.setProps({ loading: false });
    expect(wrapper.instance().stopRender).toBe(true);
  });
});
