import React from 'react';
import Dropdown from './Dropdown';
import { DropdownToggle, DropdownItem } from 'reactstrap';
import configureMockStore from 'redux-mock-store';
import { Provider } from 'react-redux';

import createFactoryConfig from '../../../core/factory/createFactoryConfig';
import FactoryProvider from '../../../core/factory/FactoryProvider';

const mockStore = configureMockStore();

const setup = props => {
  const store = mockStore({});
  const wrapper = mount(
    <Provider store={store}>
      <FactoryProvider config={createFactoryConfig({})}>
        <Dropdown {...props} />
      </FactoryProvider>
    </Provider>
  );

  return { store, wrapper };
};

describe('<Dropdown />', () => {
  it('Создание', () => {
    const { wrapper } = setup();
    expect(wrapper.find('ButtonDropdown').exists()).toBeTruthy();
  });

  it('Создание элементов списка', () => {
    const { wrapper } = setup({
      id: 'id',
      tag: 'tag',
      label: 'label',
      icon: 'icon',
      size: 'size',
      color: 'color',
      outline: 'outline',
      visible: 'visible',
      disabled: 'disabled',
      count: 'count',
      subMenu: [
        {
          src: 'PerformButton',
          id: 'id',
          tag: 'tag',
          label: 'label',
          icon: 'icon',
          size: 'size',
          color: 'color',
          outline: 'outline',
          visible: 'visible',
          disabled: 'disabled',
          count: 'count',
        },
      ],
    });

    // проверка пропсов SimpleButton
    expect(
      wrapper
        .find('SimpleButton')
        .at(0)
        .props()
    ).toEqual({
      caret: true,
      color: 'color',
      count: undefined,
      disabled: false,
      icon: 'icon',
      id: expect.any(String),
      label: 'label',
      onClick: expect.any(Function),
      outline: 'outline',
      size: 'size',
      tag: DropdownToggle,
      visible: true,
    });
  });
});
