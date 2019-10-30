import React from 'react';
import sinon from 'sinon';

import Select from './Select';
import Option from './Option';

const setup = (selectOverrides, optionOverrides) => {
  const selectProps = Object.assign(
    {
      // use this to assign some default props
    },
    selectOverrides
  );

  const optionProps = Object.assign(
    {
      // use this to assign some default props
    },
    optionOverrides
  );

  const wrapper = mount(
    <Select {...selectProps}>
      <Option value="1" label="1" {...optionProps} />
      <Option value="2" label="2" {...optionProps} />
    </Select>
  );

  return {
    optionProps,
    selectProps,
    wrapper,
  };
};

describe('<Select />', () => {
  it('creates options ', () => {
    const { wrapper } = setup();
    expect(wrapper.find('option')).toHaveLength(2);
  });

  it('sets value to option ', () => {
    const { wrapper } = setup();
    expect(
      wrapper
        .find('option')
        .first()
        .props().value
    ).toBe('1');
  });

  it('sets label to option ', () => {
    const { wrapper } = setup();
    expect(
      wrapper
        .find('option')
        .first()
        .text()
    ).toBe('1');
  });

  it('disables option when disable prop is passed to the option', () => {
    const { wrapper } = setup(null, { disabled: true });
    expect(
      wrapper
        .find('option')
        .first()
        .props().disabled
    ).toBe(true);
  });

  it('sets properties properly', () => {
    const { wrapper } = setup({ disabled: true });
    expect(
      wrapper
        .find('select')
        .first()
        .props().disabled
    ).toBe(true);
  });

  it('calls onChange', () => {
    const onChange = sinon.spy();
    const { wrapper } = setup({ onChange }, { value: '1', label: '1' });
    wrapper.find('select').simulate('change', { target: { value: '2' } });
    expect(onChange.calledOnce).toBe(true);
  });
});
