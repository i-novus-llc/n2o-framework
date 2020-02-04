import isEqual from 'lodash/isEqual';
import isArray from 'lodash/isArray';
import isBoolean from 'lodash/isBoolean';
import isFunction from 'lodash/isFunction';
import each from 'lodash/each';
import isEmpty from 'lodash/isEmpty';
import find from 'lodash/find';
import pickBy from 'lodash/pickBy';
import get from 'lodash/get';
import compact from 'lodash/compact';
import map from 'lodash/map';
import has from 'lodash/has';
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

function addError(
  fieldId,
  { text = true, severity = true },
  options = {},
  errors
) {
  if (!errors[fieldId]) {
    errors[fieldId] = [];
  }

  errors[fieldId].push({});
  const last = errors[fieldId].length - 1;

  if (isBoolean(text)) {
    errors[fieldId][last].text = options.text;
  } else {
    errors[fieldId][last].text = text;
  }

  if (isBoolean(severity)) {
    errors[fieldId][last].severity = options.severity;
  } else {
    errors[fieldId][last].severity = severity;
  }

  return errors;
}

/**
 * функция валидации
 * @param validationConfig
 * @param formName
 * @param state
 * @param isTouched
 * @returns {function(*=, *=, *, *=): Promise<void[]>}
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
                    addError(fieldId, m, options, errors);
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
          addError(fieldId, {}, options, errors);
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
            return addFieldMessage(
              formName,
              fieldId,
              message,
              isTouched
            );
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
