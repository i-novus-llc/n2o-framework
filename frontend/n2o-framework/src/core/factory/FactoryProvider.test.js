import React from 'react';
import FactoryProvider from './FactoryProvider';
import { mount } from 'enzyme/build';
import { Provider } from 'react-redux';
import createFactoryConfig from './createFactoryConfig';
import createMockStore from 'redux-mock-store';
import { CONTROLS } from './factoryLevels';
import InputText from '../../components/controls/InputText/InputText';
import NotFoundFactory from './NotFoundFactory';

const store = createMockStore()({});

const wrapper = mount(
  <Provider store={store}>
    <FactoryProvider config={createFactoryConfig({})}>
      <div>test</div>
    </FactoryProvider>
  </Provider>
);

describe('Проверка FactoryProvider', () => {
  it('проверка функции getComponent', () => {
    const factoryProvider = wrapper.find(FactoryProvider).instance();
    expect(factoryProvider.getComponent('InputText', CONTROLS)).toEqual(
      InputText
    );
    expect(factoryProvider.getComponent('InputText')).toEqual(InputText);
  });

  it('проверка функции resolveProps', () => {
    const factoryProvider = wrapper.find(FactoryProvider).instance();
    expect(
      factoryProvider.resolveProps({
        value: '',
        name: 'name',
        surname: 'surname',
      })
    ).toEqual({ value: '', name: 'name', surname: 'surname' });
    expect(
      factoryProvider.resolveProps({
        src: 'InputText',
        obj: {
          src: 'InputText',
        },
        unknown: {
          src: 'UnknownSrc',
        },
      })
    ).toEqual({
      component: InputText,
      obj: {
        component: InputText,
      },
      unknown: {
        component: NotFoundFactory,
      },
    });
  });
});
