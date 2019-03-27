import { createSelector } from 'reselect';
import { omit } from 'lodash';

/**
 *
 */
export const authSelector = state => {
  return state.user || {};
};

/**
 *
 */
export const isLoggedInSelector = createSelector(
  authSelector,
  user => user.isLoggedIn
);

/**
 *
 */
export const userSelector = createSelector(
  authSelector,
  user => omit(user, ['isLoggedIn'])
);
