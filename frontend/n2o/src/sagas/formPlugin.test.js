import { put } from 'redux-saga/effects';
import { removeMessage } from './formPlugin';
import { removeFieldMessage } from '../actions/formPlugin';

describe('Проверка саги formPlugin', () => {
  it('Сообщение должно удалиться', () => {
    const action = {
      meta: {
        form: {
          form: 'test'
        },
        field: {
          field: 'test'
        }
      }
    };
    const generator = removeMessage(action);
    const removeFieldMessageAction = put(removeFieldMessage(action.meta.form, action.meta.field));
    expect(generator.next().value).toEqual(removeFieldMessageAction);
    expect(generator.next().done).toEqual(true);
  });

  it('Сообщение не должно удалиться', () => {
    const genEmptyObject = removeMessage({});
    expect(genEmptyObject.next().value).toEqual(undefined);
    expect(genEmptyObject.next().done).toEqual(true);

    const genWithoutForm = removeMessage({
      meta: {
        field: {}
      }
    });
    expect(genWithoutForm.next().value).toEqual(undefined);
    expect(genWithoutForm.next().done).toEqual(true);

    const genWithoutField = removeMessage({
      meta: {
        form: {}
      }
    });
    expect(genWithoutField.next().value).toEqual(undefined);
    expect(genWithoutField.next().done).toEqual(true);
  });
});
