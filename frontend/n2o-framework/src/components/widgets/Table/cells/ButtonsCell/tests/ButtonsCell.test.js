import React from 'react';
import { mount } from 'enzyme';
import ButtonsCell from '../ButtonsCell';
import configureMockStore from 'redux-mock-store';
import { Provider } from 'react-redux';
import FactoryProvider from '../../../../../../core/factory/FactoryProvider';
import createFactoryConfig from '../../../../../../core/factory/createFactoryConfig';
import sinon from 'sinon';

const mockStore = configureMockStore();
const store = mockStore({});

const setup = (propOverrides = {}) => {
  const props = Object.assign({}, propOverrides);

  const wrapper = mount(
    <Provider store={store}>
      <FactoryProvider config={createFactoryConfig()}>
        <ButtonsCell {...props} />
      </FactoryProvider>
    </Provider>
  );

  return {
    props,
    wrapper,
  };
};

describe('<ButtonsCell />', () => {
  it('Проверяет создание Кнопки', () => {
    const { wrapper } = setup({
      toolbar: [
        {
          buttons: [{ src: 'LinkButton', title: 'test' }],
        },
      ],
    });

    wrapper.update();
    expect(wrapper.find('Toolbar').exists()).toBeTruthy();
  });
});
