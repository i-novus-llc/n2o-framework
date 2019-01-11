import { runSaga } from 'redux-saga';
import { getConfig } from './global';
import fetchMock from 'fetch-mock';
import sinon from 'sinon';

fetchMock
  .get('n2o/config', () => {
    return {
      test: 'test'
    };
  })
  .restore();

describe('Проверка саги global', () => {
  it('Проверка getConfig', () => {
    const dispatched = [];
    // sinon
    //   .stub(
    //     () => ({
    //       some: 'value'
    //     }),
    //     'fetchSaga'
    //   )
    //   .callsFake(() => ({
    //     json: () => ({
    //       some: 'value'
    //     })
    //   }));
    const saga = runSaga(
      {
        dispatch: action => dispatched.push(action),
        getState: () => ({ value: 'test' })
      },
      getConfig,
      {
        payload: {
          params: {
            locale: 'ru_RU'
          }
        }
      }
    );
    console.log(saga.done);
    console.log(dispatched[0].payload);
  });
});
