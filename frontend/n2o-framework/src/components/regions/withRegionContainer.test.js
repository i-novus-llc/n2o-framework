import React from 'react';
import { Provider } from 'react-redux';

import configureStore from '../../store';
import history from '../../history';
import withRegionContainer from './withRegionContainer';

const ButtonsComponent = ({ changeActiveEntity }) => (
  <div>
    <button onClick={() => changeActiveEntity('item1')}>first</button>
    <button onClick={() => changeActiveEntity('item2')}>second</button>
  </div>
);

const regionId = 'someRegion';
const store = configureStore({}, history, {});
const setup = propsOverride => {
  const props = {
    id: regionId,
    list: [
      {
        id: 'item1',
      },
      {
        id: 'item2',
      },
    ],
  };

  const Component = withRegionContainer({ listKey: 'list' })(ButtonsComponent);

  return mount(
    <Provider store={store}>
      <Component {...props} {...propsOverride} />
    </Provider>
  );
};

describe('тесты на hoc withRegionContainer', () => {
  const wrapper = setup();

  it('регион регистрируется', () => {
    expect(store.getState().regions).toEqual({
      [regionId]: {
        regionId,
        isInit: true,
        list: [
          {
            id: 'item1',
          },
          {
            id: 'item2',
          },
        ],
      },
    });
  });

  it('изменяется активная сущность', () => {
    wrapper
      .find('button')
      .first()
      .simulate('click');
    expect(store.getState().regions[regionId].activeEntity).toBe('item1');
    wrapper
      .find('button')
      .last()
      .simulate('click');
    expect(store.getState().regions[regionId].activeEntity).toBe('item2');
  });
});
