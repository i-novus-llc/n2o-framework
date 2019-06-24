import createActionHelper from "n2o/lib/actions/createActionHelper";

export const ADD = "n2o/notifications/ADD";
export const DESTROY = "n2o/notifications/DESTROY";
export const CLEAR_ALL = "n2o/notifications/CLEAR_ALL";
export const SET_COUNTER = "n2o/notifications/SET_COUNTER";

export function add(
  id,
  { text, icon, image, title, date, close, delay }
) {
  return createActionHelper(ADD)({
    id,
    text,
    icon,
    image,
    title,
    date,
    close,
    delay
  });
}

export function destroy(id) {
  return createActionHelper(DESTROY)({
    id
  });
}

export function clearAll() {
  return createActionHelper(CLEAR_ALL)();
}

export function setCounter(counterId, value) {
  return createActionHelper(SET_COUNTER)({
    counterId,
    value
  });
}
