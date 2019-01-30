import React from 'react';
import { Provider } from 'react-redux';
import configureMockStore from 'redux-mock-store';
import Actions from '../Actions';

const setup = (props, state) => {
  const mockStore = configureMockStore(state);
  const store = mockStore();
  const wrapper = mount(
    <Provider store={store}>
      <Actions {...props} />
    </Provider>
  );

  return {
    wrapper,
    store
  };
};

describe('Actrions', () => {
  it('Dropdown скрыт если все sub-menu скрыты', () => {
    const testProps = {
      toolbar: [
        {
          id: '1',
          subMenu: [
            {
              id: 'testBtn29',
              visible: false
            },
            {
              id: 'testBtn29',
              visible: false
            }
          ]
        }
      ],
      containerKey: 'test'
    };

    const { wrapper, store } = setup(testProps);

    expect(wrapper.find('.dropdown-toggle').exists()).toEqual(false);
  });
});
