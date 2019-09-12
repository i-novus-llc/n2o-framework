import {
  isObject,
  omit,
  pick,
  isEqual,
  isArray,
  isBoolean,
  isFunction,
  each,
  isEmpty,
  find,
  pickBy,
  get,
  compact,
  map,
  has,
} from 'lodash';
import { isPromise } from '../../tools/helpers';
import * as presets from './presets';
import { addFieldMessage, removeFieldMessage } from '../../actions/formPlugin';
import { batchActions } from 'redux-batched-actions/lib/index';

function findPriorityMessage(messages) {
  return (
    find(messages, { severity: 'danger' }) ||
    find(messages, { severity: 'warning' }) ||
    find(messages, { severity: 'success' })
  );
}

/**
 * есть ли ошибки или нет
 * @param messages
 * @returns {*}
 */
function hasError(messages) {
  return [].concat
    .apply([], Object.values(messages))
    .reduce((res, msg) => msg.severity === 'danger' || res, false);
}

export default function createValidator(
  validationConfig = {},
  formName,
  state,
  fields
) {
  return {
    asyncValidate: validateField(validationConfig, formName, state),
    asyncBlurFields: fields || [],
  };
}

/**
 * функция валидации
 * @param validationConfig
 * @param formName
 * @param state
 * @param isTouched
 * @returns {Promise<any[]>}
 */
export const validateField = (
  validationConfig,
  formName,
  state,
  isTouched = false
) => (values, dispatch) => {
  const registeredFields = get(state, ['form', formName, 'registeredFields']);
  const fields = get(state, ['form', formName, 'fields']);
  const validation = pickBy(validationConfig, (value, key) =>
    get(registeredFields, `${key}.visible`, true)
  );
  const errors = {};
  const promiseList = [Promise.resolve()];
  const addError = (
    fieldId,
    { text = true, severity = true },
    options = {}
  ) => {
    !errors[fieldId] && (errors[fieldId] = []);
    errors[fieldId].push({});
    const last = errors[fieldId].length - 1;
    isBoolean(text)
      ? (errors[fieldId][last].text = options.text)
      : (errors[fieldId][last].text = text);
    isBoolean(severity)
      ? (errors[fieldId][last].severity = options.severity)
      : (errors[fieldId][last].severity = severity);
  };
  each(validation, (validationList, fieldId) => {
    if (isArray(validationList)) {
      each(validationList, options => {
        const isValid =
          isFunction(presets[options.type]) &&
          presets[options.type](fieldId, values, options, dispatch);
        if (isPromise(isValid)) {
          promiseList.push(
            new Promise(resolve => {
              isValid.then(
                resp => {
                  each(resp && resp.message, m => {
                    addError(fieldId, m, options);
                  });
                  resolve();
                },
                () => {
                  resolve();
                }
              );
            })
          );
        } else if (!isValid) {
          addError(fieldId, {}, options);
        }
      });
    }
  });
  return Promise.all(promiseList).then(() => {
    const messagesAction = compact(
      map(errors, (messages, fieldId) => {
        if (!isEmpty(messages)) {
          const message = findPriorityMessage(messages);
          if (
            !isEqual(message, get(registeredFields, [fieldId, 'message'])) ||
            !get(fields, [fieldId, 'touched'])
          ) {
            return addFieldMessage(formName, fieldId, message, isTouched);
          }
        }
      })
    );

    map(registeredFields, (field, key) => {
      if (!has(errors, key) && get(field, 'message', null)) {
        messagesAction.push(removeFieldMessage(formName, key));
      }
    });

    messagesAction && dispatch(batchActions(messagesAction));

    return hasError(errors);
  });
};
