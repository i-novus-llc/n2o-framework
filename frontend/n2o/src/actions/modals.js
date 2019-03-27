import { INSERT, DESTROY, HIDE, SHOW, SHOW_PROMPT, HIDE_PROMPT, CLOSE } from '../constants/modals';
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
 */
export function insertModal(
  name,
  visible,
  title,
  size,
  closeButton,
  pageId,
  src
) {
  return createActionHelper(INSERT)({
    name,
    visible,
    title,
    size,
    closeButton,
    pageId,
    src,
  });
}

/**
 * Показать модальное окно
 * @param name
 */
export function showModal(name) {
  return createActionHelper(SHOW)({ name });
}

/**
 * Скрыть модальное окно
 * @param name
 */
export function hideModal(name) {
  return createActionHelper(HIDE)({ name });
}

/**
 * Удалить модальное окно
 * @returns {*}
 */
export function destroyModal() {
  return createActionHelper(DESTROY)();
}

export function closeModal(name, prompt) {
  return createActionHelper(CLOSE)({ name, prompt });
}

export function showPrompt(name) {
  return createActionHelper(SHOW_PROMPT)({ name });
}

export function hidePrompt(name) {
  return createActionHelper(HIDE_PROMPT)({ name });
}
