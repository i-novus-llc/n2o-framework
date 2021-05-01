import React from 'react';
import configureMockStore from 'redux-mock-store';
import { Provider } from 'react-redux';

import perform from '../../../impl/actions/perform/perform';

const mockStore = configureMockStore();

describe('Тестирование имплов', () => {
  it('Тестирование перформа', () => {
    const store = mockStore({});
    perform({ dispatch: store.dispatch, type: 'DUMMY' });
    expect(store.getActions()[0]).toMatchObject({ type: 'DUMMY' });
  });
});
