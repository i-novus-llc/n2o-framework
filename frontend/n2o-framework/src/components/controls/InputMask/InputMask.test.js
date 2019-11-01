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
    wrapper,
  };
};

describe('<InputMask />', () => {
  it('has input', () => {
    const { wrapper } = setup();
    expect(wrapper.find('input')).toHaveLength(1);
  });

  it('binds value', () => {
    const { wrapper } = setup({ mask: '9-9', value: '1-1' });
    expect(wrapper.state().value).toBe('11');
  });

  it('обрежет значение, если оно больше маски', () => {
    const { wrapper } = setup({
      mask: '+7 (999) 999-99-99',
      value: '89119468608',
    });
    expect(wrapper.state().value).toBe('9119468608');
  });
});
