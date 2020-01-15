import React from 'react';
import ButtonField from "./ButtonField";

const setup = () => {
  const props = {
    label: 'Поле "ButtonField"'
  };

  return mount(
    <ButtonField {...props} />
  )
};

describe('<ButtonField />', () => {
  it('компонент отрисовывается', () => {
    const wrapper = setup();

    expect(wrapper.find('.n2o-button-field').exists()).toBeTruthy();
  });
});