import { createSelector } from 'reselect';
import find from 'lodash/find';
import get from 'lodash/get';

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

export const makeShowPromptByName = name =>
  createSelector(
    modalsSelector,
    modalsState =>
      get(find(modalsState, modal => modal.name === name), 'showPrompt', null)
  );
