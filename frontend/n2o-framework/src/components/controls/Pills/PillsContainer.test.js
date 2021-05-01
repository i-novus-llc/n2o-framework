import React from 'react';
import sinon from 'sinon';
import PillsContainer from './PillsContainer';
import { mount } from 'enzyme';

describe('<PillsContainer />', () => {
  it('Создание компонента', () => {
    const wrapper = mount(<PillsContainer />);
    expect(wrapper.exists()).toBeTruthy();
  });

  it('Вызов onChange', () => {
    const onChange = sinon.spy();
    const wrapper = mount(
      <PillsContainer
        data={[{ label: 'text', value: 1 }]}
        onChange={onChange}
      />
    );
    wrapper.find('NavLink').simulate('click');
    expect(onChange.calledOnce).toBe(true);
    expect(onChange.getCalls()[0].lastArg).toEqual([1]);
  });
});
