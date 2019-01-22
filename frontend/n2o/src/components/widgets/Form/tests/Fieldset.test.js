import React from 'react';
import { mount } from 'enzyme';
import configureMockStore from 'redux-mock-store';
import { Provider } from 'react-redux';
import Fieldset from '../Fieldset';
import StandardFieldset from '../fieldsets/BlankFieldset';
import {
  ENABLE_FIELDS,
  DISABLE_FIELDS,
  SHOW_FIELDS,
  HIDE_FIELDS
} from '../../../../constants/formPlugin';

const mockStore = configureMockStore();

const defaultStateObj = {
  models: {
    resolve: {}
  }
};

const setup = (storeObj, propOverrides = {}) => {
  const props = Object.assign(
    {
      form: '__form',
      component: StandardFieldset,
      visible: '`id == 2`',
      enabled: '`id == 3`',
      dependency: [
        {
          type: 'reRender',
          on: ['id']
        }
      ],
      rows: []
    },
    propOverrides
  );

  const store = mockStore(storeObj || defaultStateObj);

  const wrapper = mount(
    <Provider store={store}>
      <Fieldset {...props} />
    </Provider>
  );

  return {
    props,
    wrapper,
    store
  };
};

describe('<FieldSelts />', () => {
  it('проверяет создание элемента', () => {
    const { wrapper } = setup();
    expect(wrapper.find(StandardFieldset).exists()).toBeTruthy();
  });

  it('Проверяем изменение видимости элемента', () => {
    const { wrapper, store } = setup({
      models: {
        resolve: {
          __form: {
            id: 2
          }
        }
      }
    });
    wrapper.update();
    expect(store.getActions()[0].type).toEqual(DISABLE_FIELDS);
    expect(store.getActions()[1].type).toEqual(SHOW_FIELDS);
  });
  it('Проверяем изменение enable элемента', () => {
    const { wrapper, store } = setup({
      models: {
        resolve: {
          __form: {
            id: 3
          }
        }
      }
    });
    wrapper.update();
    expect(store.getActions()[0].type).toEqual(ENABLE_FIELDS);
    expect(store.getActions()[1].type).toEqual(HIDE_FIELDS);
  });
});
