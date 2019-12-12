import { createSelector } from 'reselect';

/*
 селектор для всех кнопок
 */
const toolbarSelector = state => {
  return state.toolbar || {};
};

const getContainerButtons = containerKey =>
  createSelector(
    toolbarSelector,
    toolbar => toolbar[containerKey] || {}
  );

/**
 * селектор для кнопки по уникальному ключу
 * @param key
 */

const makeButtonByKeyAndIdSelector = (key, id) =>
  createSelector(
    getContainerButtons(key),
    containerButtons => {
      return containerButtons[id] || {};
    }
  );

/**
 * селектор для того, чтобы узнать зарегистрирована кнопка или нет
 * @param key
 */
const isInitSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.isInit;
    }
  );

/**
 *  селектор видимости кнопки
 * @param key
 */
const isVisibleSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.visible;
    }
  );

/*
 * селектор блокировки кнопки
 */
const isDisabledSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.disabled;
    }
  );

/**
 * селектор пазмера кнопки
 * @param key
 */
const sizeSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.size;
    }
  );

/**
 * селектор цвета кнопки
 * @param key
 */
const colorSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.color;
    }
  );

/**
 * селектор счетчика кнопки
 * @param key
 */
const countSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.count;
    }
  );

/**
 * селектор имени кнопки
 * @param key
 */
const titleSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.title;
    }
  );

/**
 * селектор посказаки кнопки
 * @param key
 * @param id
 */
const hintSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.hint;
    }
  );

/**
 * селектор расположения посказаки кнопки
 * @param key
 * @param id
 */

const hintPositionSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.hintPosition;
    }
  );

/**
 * селектор иконки кнопки
 * @param key
 */
const iconSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.icon;
    }
  );

/**
 * селектор стиля кнопки
 * @param key
 */
const styleSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.style;
    }
  );

/**
 * селектор класса кнопки
 * @param key
 */
const classSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.className;
    }
  );

/**
 * селектор выполнения
 * @param key
 */
const isLoading = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.loading;
    }
  );
/**
 * селектор ошибок экшена кнопки
 * @param key
 */
const errorSelector = (key, id) =>
  createSelector(
    makeButtonByKeyAndIdSelector(key, id),
    button => {
      return button.error;
    }
  );

export {
  toolbarSelector,
  isVisibleSelector,
  sizeSelector,
  colorSelector,
  isDisabledSelector,
  titleSelector,
  countSelector,
  hintSelector,
  iconSelector,
  classSelector,
  styleSelector,
  isInitSelector,
  isLoading,
  errorSelector,
  getContainerButtons,
  hintPositionSelector,
};
