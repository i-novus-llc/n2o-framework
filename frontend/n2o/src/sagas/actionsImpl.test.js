import createSagaMiddleware, { fork } from 'redux-saga';
import sagas from '../sagas.js';
import configureStore from 'redux-mock-store';
import { handleAction, resolveMapping, handleInvoke } from './actionsImpl';
import { START_INVOKE, SUCCESS_INVOKE, FAIL_INVOKE } from '../constants/actionImpls';
import { CALL_ACTION_IMPL } from '../constants/toolbar';

const setupHandleAction = () => {
  return handleAction();
};
describe('Проверка саги actionsImpl', () => {
  it('Проверка вызова handleAction', () => {
    // const gen = setupHandleAction();
    // console.log(gen.next().value);
    // console.log(gen.next().value);
    // console.log(gen.next().value);
  });

  it('Проверка resolveMapping', () => {
    // const path = resolveMapping(
    //   {
    //     method: 'POST',
    //     pathMapping: {
    //       proto_patients_id: {
    //         link: 'models.resolve["proto_patients"].id'
    //       }
    //     },
    //     url: 'n2o/data/proto/patients/:proto_patients_id/vip'
    //   },
    //   {
    //     models: {
    //       resolve: {
    //         proto_patients: {
    //           id: 20
    //         }
    //       }
    //     }
    //   }
    // );
    // console.log(path.next().value);
    // console.log(path.next().value);
    // expect(
    //   resolveMapping({
    //     method: 'POST',
    //     pathMapping: {
    //       test: {
    //         link: 'models.test'
    //       }
    //     },
    //     url: 'n2o/data/:test'
    //   }, {
    //     models: {
    //       test: 'test_is_passed'
    //     }
    //   }).next()
    // ).toEqual('n2o/data/test_is_passed')
  });
});
