import { createSelector } from 'reselect';

/**
 * селектор модольных окон
 * @param state
 */
export const modalsSelector = state => {
  return state.modals || {};
};

/**
 * селектор модального окна по индексу
 * @param i
 */
export const makeModalsbyName = i =>
  createSelector(
    modalsSelector,
    modalsState => {
      return modalsState[i];
    }
  );
