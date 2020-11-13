import { call, put, select, takeEvery } from 'redux-saga/effects';
import values from 'lodash/values';
import filter from 'lodash/filter';
import get from 'lodash/get';
import reduce from 'lodash/reduce';
import first from 'lodash/first';
import some from 'lodash/some';
import every from 'lodash/some';

import { MAP_URL } from '../constants/regions';
import { DATA_SUCCESS } from '../constants/widgets';
import { getLocation, rootPageSelector } from '../selectors/global';
import { makePageRoutesByIdSelector } from '../selectors/pages';
import { regionsSelector } from '../selectors/regions';
import { widgetsSelector } from '../selectors/widgets';
import { setActiveEntity } from '../actions/regions';

import { routesQueryMapping } from './widgets';

function* mapUrl() {
  const state = yield select();
  const location = yield select(getLocation);
  const rootPageId = yield select(rootPageSelector);
  const routes = yield select(makePageRoutesByIdSelector(rootPageId));

  if (routes) {
    yield call(routesQueryMapping, state, routes, location);
  }
}

function* switchTab() {
  const state = yield select();

  const regions = regionsSelector(state);
  const widgets = widgetsSelector(state);

  const tabsRegions = filter(values(regions), region => region.tabs);

  const hasActiveEntity = every(
    tabsRegions,
    region => region.activeEntity !== undefined
  );

  const getWidgetsIds = content =>
    reduce(content, (acc, meta) => acc.concat(meta.id), []);

  const activeEntityWidgetsIds = reduce(
    tabsRegions,
    (acc, tabsRegion) => {
      const { tabs, activeEntity } = tabsRegion;

      const activeTab = first(filter(tabs, tab => tab.id === activeEntity));
      const activeContent = get(activeTab, 'content');

      acc.push({
        widgetsIds: getWidgetsIds(activeContent),
      });

      return acc;
    },
    []
  );

  for (let index = 0; index <= activeEntityWidgetsIds.length - 1; index += 1) {
    const { widgetsIds } = activeEntityWidgetsIds[index];

    const atLeastOneVisibleWidget = some(
      widgetsIds,
      widgetId => widgets[widgetId]['isVisible'] === true
    );

    if (!hasActiveEntity || !atLeastOneVisibleWidget) {
      for (let index = 0; index <= tabsRegions.length - 1; index += 1) {
        const { regionId, tabs } = tabsRegions[index];

        const openTab = filter(tabs, tab => tab.opened);
        const activeEntity = get(first(openTab), 'id');

        yield put(setActiveEntity(regionId, activeEntity));
      }
    }
  }
  mapUrl();
}

export default [takeEvery(MAP_URL, mapUrl), takeEvery(DATA_SUCCESS, switchTab)];
