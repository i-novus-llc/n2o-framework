import fetchSaga from './fetch';
import { FETCH_START } from '../constants/fetch';
import { FETCH_APP_CONFIG } from '../core/api';
import fetchMock from 'fetch-mock';

fetchMock.get('n2o/config', () => {
  return {
    test: 'test'
  };
});

describe('Проверка саги fetch', () => {
  it('Проверка ответа вхождения', () => {
    const gen = fetchSaga(FETCH_APP_CONFIG, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    expect(gen.next().value.PUT.action).toEqual({
      meta: {},
      type: FETCH_START,
      payload: {
        fetchType: FETCH_APP_CONFIG,
        options: {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      }
    });
  });
  it('Проверка отправки запроса', () => {
    const gen = fetchSaga(FETCH_APP_CONFIG, {
      locale: 'ru_RU'
    });
    gen.next();
    console.log(gen.next().value.CALL);
    console.log(gen.next().value.PUT);
    console.log(gen.next());
    console.log(gen.next());
  });
});
