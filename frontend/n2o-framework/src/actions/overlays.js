import {
  INSERT,
  INSERT_DIALOG,
  DESTROY,
  DESTROY_OVERLAYS,
  HIDE,
  SHOW,
  SHOW_PROMPT,
  HIDE_PROMPT,
  CLOSE,
} from '../constants/overlays';
import createActionHelper from './createActionHelper';

/**
 * Регистрация оверлея в редаксе
 * @param name
 * @param visible
 * @param mode
 * @param {Object} addition - объект с дополнительные параметрами
 */
export function insertOverlay(name, visible, mode, addition) {
  return createActionHelper(INSERT)({
    name,
    visible,
    mode,
    ...addition,
  });
}

export function insertDialog(name, visible, props) {
  return createActionHelper(INSERT_DIALOG)({
    name,
    visible,
    mode: 'dialog',
    ...props,
  });
}

/**
 * Показать окно
 * @param name
 */
export function showOverlay(name) {
  return createActionHelper(SHOW)({ name });
}

/**
 * Скрыть окно
 * @param name
 */
export function hideOverlay(name) {
  return createActionHelper(HIDE)({ name });
}

/**
 * Удалить окно
 * @returns {*}
 */
export function destroyOverlay() {
  return createActionHelper(DESTROY)();
}

export function destroyOverlays(count) {
  return createActionHelper(DESTROY_OVERLAYS)({ count });
}

/**
 * События при попытке закрыть окно
 * @param name
 * @param prompt
 */
export function closeOverlay(name, prompt) {
  return createActionHelper(CLOSE)({ name, prompt });
}

/**
 * Показать подтверждение закрытия окна
 * @param name
 */
export function showPrompt(name) {
  return createActionHelper(SHOW_PROMPT)({ name });
}

/**
 * Скрыть подтверждение закрытия окна
 * @param name
 */
export function hidePrompt(name) {
  return createActionHelper(HIDE_PROMPT)({ name });
}
