import React from 'react';
import { AdvancedTableFilterPopup } from './AdvancedTableFilterPopup';

const setup = propsOverride => {
  return mount(<AdvancedTableFilterPopup {...propsOverride} />);
};

describe('<AdvancedTableFilterPopup />', () => {
  it('компонент отрисовывается', () => {
    const wrapper = setup();

    expect(
      wrapper.find('.n2o-advanced-table-filter-dropdown-popup').exists()
    ).toBeTruthy();
    expect(wrapper.find('InputText').exists()).toBeTruthy();
  });

  it('отрисовывается переданный контрол', () => {
    const wrapper = setup({
      component: () => <textarea />,
    });

    expect(wrapper.find('textarea').exists()).toBeTruthy();
  });
});
