import { takeEvery, call, select } from 'redux-saga/effects';

import { routesQueryMapping } from "./widgets";
import { MAP_URL } from "../constants/regions";
import { getLocation, rootPageSelector } from "../selectors/global";
import { makePageRoutesByIdSelector } from "../selectors/pages";

function* mapUrl() {
  const state = yield select();
  const location = yield select(getLocation);
  const rootPageId = yield select(rootPageSelector);
  const routes = yield select(makePageRoutesByIdSelector(rootPageId));

  yield call(routesQueryMapping, state, routes, location);
}

export default [takeEvery(MAP_URL, mapUrl)];