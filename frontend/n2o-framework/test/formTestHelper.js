import React from 'react';
import { some, isEqual, get, has } from 'lodash';
import Factory from '../src/core/factory/Factory';
import FactoryProvider from '../src/core/factory/FactoryProvider';
import createFactoryConfig from '../src/core/factory/createFactoryConfig';
import { WIDGETS } from '../src/core/factory/factoryLevels';
import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import rootReducer from '../src/reducers';
import history from '../src/history';

const setValueToForm = (override = {}) => ({
  src: 'FormWidget',
  form: {
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
                    ...override,
                  },
                ],
              },
            ],
          },
        ],
      },
    ],
  },
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

/**
 * вспомогательная функция
 * для взятия visited, active, touched
 * при событии onFocus, onBlur
 * @param store
 * @returns {*}
 */
export const getField = store =>
  get(store.getState(), 'form["Page_Form"].fields.testControl', false);

/**
 * Вспомогательная функция для интеграционного тестировани
 * @param props - данные компонента, обязательно указывать src
 * @returns {{store, wrapper: *}}
 */

export default props => {
  const actions = [];

  // мидлваре слушаем и пушем actions в массив
  const actionLogger = store => next => action => {
    actions.push(action);
    return next(action);
  };

  const store = createStore(
    rootReducer(history),
    applyMiddleware(actionLogger)
  );

  const wrapper = mount(
    <Provider store={store}>
      <FactoryProvider config={createFactoryConfig({})}>
        <Factory level={WIDGETS} {...setValueToForm(props)} id="Page_Form" />
      </FactoryProvider>
    </Provider>
  );

  return {
    actions,
    store,
    wrapper,
  };
};
