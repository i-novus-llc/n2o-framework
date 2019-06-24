import { createSelector } from "reselect";
import { find } from "lodash";

export const notificationsSelector = state => {
  return state.notifications || {};
};

export const stackSelector = createSelector(
  notificationsSelector,
  state => state.stack
);

export const countersSelector = createSelector(
  notificationsSelector,
  state => state.counters
);

export const getNotify = id =>
  createSelector(
    stackSelector,
    state => find(state, { id })
  );

export const getCounter = id =>
  createSelector(
    countersSelector,
    state => state[id]
  );
