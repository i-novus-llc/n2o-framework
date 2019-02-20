import React from 'react';

import InputMask from './InputMask';

const setup = propOverrides => {
  const props = Object.assign(
    {
      // use this to assign some default props
    },
    propOverrides
  );

  const wrapper = mount(<InputMask {...props} />);

  return {
    props,
    wrapper
  };
};

describe('<InputMask />', () => {
  it('has input', () => {
    const { wrapper } = setup();
    expect(wrapper.find('input')).toHaveLength(1);
  });

  it('binds value', () => {
    const { wrapper } = setup({ mask: '9-9', value: '1-1' });
    expect(wrapper.state().value).toBe('1-1');
  });
});
