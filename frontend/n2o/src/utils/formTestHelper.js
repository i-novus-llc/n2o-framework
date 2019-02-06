import React from 'react';
import { some, isEqual } from 'lodash';
import Factory from '../../src/core/factory/Factory';
import FactoryProvider from '../../src/core/factory/FactoryProvider';
import createFactoryConfig from '../../src/core/factory/createFactoryConfig';
import { WIDGETS } from '../../src/core/factory/factoryLevels';
import { Provider } from 'react-redux';
import { createStore, combineReducers } from 'redux';
import rootReducer from '../../src/reducers';

const setValueToForm = (override = {}) => ({
  src: 'FormWidget',
  form: {
    fetchOnInit: false,
    fieldsets: [
      {
        src: 'StandardFieldset',
        rows: [
          {
            cols: [
              {
                fields: [
                  {
                    id: 'testControl',
                    dependency: [],
                    control: {},
                    ...override
                  }
                ]
              }
            ]
          }
        ]
      }
    ]
  }
});

/**
 * вспомогательная функция для тестов
 * смотрим совпадение в коллекции
 * @param collection
 * @param object
 * @returns {*}
 */
export const toMathInCollection = (collection, object) =>
  some(collection, item => isEqual(item, object));

const lastAction = (state = null, action) => action;
/**
 * Вспомогательная функция для интеграционного тестировани
 * @param props - данные компонента, обязательно указывать src
 * @returns {{store, wrapper: *}}
 */
export default props => {
  const actions = [];

  // слушаем и закидываем все экшены в массив
  const reducer = combineReducers({
    rootReducer,
    lastAction
  });
  const store = createStore(reducer);

  const wrapper = mount(
    <Provider store={store}>
      <FactoryProvider config={createFactoryConfig({})}>
        <Factory level={WIDGETS} {...setValueToForm(props)} id="Page_Form" />
      </FactoryProvider>
    </Provider>
  );

  store.subscribe(() => {
    actions.push(store.getState().lastAction);
  });

  return {
    actions,
    store,
    wrapper
  };
};
