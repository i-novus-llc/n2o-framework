import {
  INSERT,
  DESTROY,
  HIDE,
  SHOW,
  SHOW_PROMPT,
  HIDE_PROMPT,
  CLOSE,
} from '../constants/overlays';
import createActionHelper from './createActionHelper';

/**
 * Регистрация модального окна в редаксе
 * @param name
 * @param visible
 * @param title
 * @param size
 * @param closeButton
 * @param pageId
 * @param src
 * @param mod
 */
export function insertOverlay(
  name,
  visible,
  title,
  size,
  closeButton,
  pageId,
  src,
  mod
) {
  return createActionHelper(INSERT)({
    name,
    visible,
    title,
    size,
    closeButton,
    pageId,
    src,
    mod,
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
