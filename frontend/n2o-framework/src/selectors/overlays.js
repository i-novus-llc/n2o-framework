import { createSelector } from 'reselect';
import { find } from 'lodash';

/**
 * селектор модольных окон
 * @param state
 */
export const overlaysSelector = state => {
  return state.overlays || {};
};

/**
 * селектор модального окна по индексу
 * @param i
 */
export const makeOverlaysbyName = i =>
  createSelector(
    overlaysSelector,
    overlaysState => {
      return overlaysState[i];
    }
  );

export const makeShowPromptByName = name =>
  createSelector(
    overlaysSelector,
    overlaysState => {
      console.log('pointt', overlaysState);
      return find(overlaysState, overlay => overlay.name === name).showPrompt;
    }
  );
