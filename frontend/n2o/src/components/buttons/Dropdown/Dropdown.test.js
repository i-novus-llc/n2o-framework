import React from 'react';
import Dropdown from './Dropdown';
import configureMockStore from 'redux-mock-store';
import { Provider } from 'react-redux';

const mockStore = configureMockStore();

const setup = props => {
  const store = mockStore({});
  const wrapper = mount(
    <Provider store={store}>
      <Dropdown {...props} />
    </Provider>
  );

  return { store, wrapper };
};

describe('<Dropdown />', () => {
  it('Создание', () => {
    const { wrapper } = setup();
    expect(wrapper.find('ButtonDropdown').exists()).toBeTruthy();
  });
});
