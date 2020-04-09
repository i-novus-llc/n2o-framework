import {
  INSERT_MODAL,
  INSERT_DRAWER,
  DESTROY,
  HIDE,
  SHOW,
  SHOW_PROMPT,
  HIDE_PROMPT,
  CLOSE,
} from '../constants/overlays';
import createActionHelper from './createActionHelper';

/**
 * Регистрация модалки в редаксе
 * @param name
 * @param visible
 * @param mode
 * @param rest
 */
export function insertModal(name, visible, mode, ...rest) {
  return createActionHelper(INSERT_MODAL)({
    name,
    visible,
    mode: 'modal',
    ...rest,
  });
}

/**
 * Регистрация дравера в редаксе
 * @param name
 * @param visible
 * @param mode
 * @param rest
 */
export function insertDrawer(name, visible, mode, ...rest) {
  return createActionHelper(INSERT_DRAWER)({
    name,
    visible,
    mode: 'drawer',
    ...rest,
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
