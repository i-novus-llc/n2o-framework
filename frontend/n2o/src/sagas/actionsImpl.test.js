import { handleAction, resolveMapping, handleInvoke } from './actionsImpl';
import { START_INVOKE, SUCCESS_INVOKE, FAIL_INVOKE } from '../constants/actionImpls';
import { CALL_ACTION_IMPL } from '../constants/toolbar';

const handleActionConfig = {
  meta: {},
  payload: {
    actionSrc: 'perform',
    options: {
      actionId: 'update',
      buttonId: 'update',
      containerKey: '__patients',
      type: 'n2o/modals/INSERT',
      validate: true
    }
  },
  type: 'n2o/actionsImpl/CALL_ACTION_IMPL'
};

const setupHandleAction = () => {
  return handleAction(handleActionConfig);
};
describe('Проверка саги actionsImpl', () => {
  it('Проверка вызова handleAction', () => {
    const gen = setupHandleAction();
    // console.log(gen.next().value)
    // console.log(gen.next().value)
    // console.log(gen.next().value)
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
