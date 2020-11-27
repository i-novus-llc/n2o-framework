import React from 'react';
import Factory from './Factory';
import InputTextJson from '../../components/controls/InputText/InputText.meta';
import InputText from '../../components/controls/InputText/InputText';
import mockStore from 'redux-mock-store';
import { Provider } from 'react-redux';
import FactoryProvider from './FactoryProvider';
import createFactoryConfig from './createFactoryConfig';

const setup = props => {
  const store = mockStore()({});
  return mount(
    <Provider store={store}>
      <FactoryProvider config={createFactoryConfig({})}>
        <Factory {...props} />
      </FactoryProvider>
    </Provider>
  );
};

describe('Провера Factory', () => {
  it('Создает нужный компонент', () => {
    const wrapper = setup({
      src: 'InputText',
      ...InputTextJson,
    });
    expect(wrapper.find(InputText).exists()).toEqual(true);
  });
  it('Вернет null, если компонент не найден', () => {
    const wrapper = setup({
      src: 'UnknownSrc',
    });
    expect(wrapper.find(Factory).html()).toEqual(null);
  });
});
