import { removeMessage } from './formPlugin';
import { REMOVE_FIELD_MESSAGE } from '../constants/formPlugin';

describe('Проверка саги formPlugin', () => {
  it('Проверка removeMessage', () => {
    expect(
      removeMessage({
        meta: {
          form: {
            name: 'test form'
          },
          field: {
            fieldName: 'test field'
          }
        }
      }).next().value.PUT.action
    ).toEqual({
      type: REMOVE_FIELD_MESSAGE,
      payload: {
        name: {
          fieldName: 'test field'
        },
        form: {
          name: 'test form'
        }
      },
      meta: {
        form: {
          name: 'test form'
        }
      }
    });
  });
});
