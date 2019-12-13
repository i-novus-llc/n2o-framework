import React from 'react';
import withTooltip from './withTooltip';
import UncontrolledTooltip from 'reactstrap/lib/UncontrolledTooltip';

const setup = propsOverride => {
  return mount(
    withTooltip(<div className={'test'}>test</div>, 'test hint', 123)
  );
};

describe('Проверка withTooltip', () => {
  it('вернет компонент с тултипом', () => {
    //  const wrapper = setup();
    // expect(wrapper.find(UncontrolledTooltip).exists()).toEqual(true);
  });
});
