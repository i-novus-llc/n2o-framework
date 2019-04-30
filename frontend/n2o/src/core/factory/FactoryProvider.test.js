import React from 'react';
import FactoryProvider from './FactoryProvider';
import { mount } from 'enzyme/build';
import { IntlProvider } from 'react-intl';
import { Provider } from 'react-redux';
import createFactoryConfig from './createFactoryConfig';
import createMockStore from 'redux-mock-store';
import { CONTROLS } from './factoryLevels';
import InputText from '../../components/controls/InputText/InputText';
import NotFoundFactory from './NotFoundFactory';

const store = createMockStore()({});

const wrapper = mount(
  <IntlProvider locale="ru" messages={{}}>
    <Provider store={store}>
      <FactoryProvider config={createFactoryConfig({})}>
        <div>test</div>
      </FactoryProvider>
    </Provider>
  </IntlProvider>
);

describe('Проверка FactoryProvider', () => {
  it('проверка функции getComponent', () => {
    const factoryProvider = wrapper.find(FactoryProvider).instance();

    const Comp = factoryProvider.getComponent('InputText', CONTROLS);
    const Comp2 = factoryProvider.getComponent('InputText');

    expect(
      mount(<Comp />)
        .find(InputText)
        .exists()
    ).toBeTruthy();
    expect(
      mount(<Comp2 />)
        .find(InputText)
        .exists()
    ).toBeTruthy();
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

    const resolvedObj = factoryProvider.resolveProps({
      src: 'InputText',
      obj: {
        src: 'InputText',
      },
      unknown: {
        src: 'UnknownSrc',
      },
    });

    expect(
      mount(React.createElement(resolvedObj.component))
        .find(InputText)
        .exists()
    ).toBeTruthy();

    expect(
      mount(React.createElement(resolvedObj.obj.component))
        .find(InputText)
        .exists()
    ).toBeTruthy();

    expect(
      mount(React.createElement(resolvedObj.unknown.component))
        .find(NotFoundFactory)
        .exists()
    ).toBeTruthy();
  });
});
