import { call, put, select, takeEvery } from 'redux-saga/effects';
import values from 'lodash/values';
import filter from 'lodash/filter';
import get from 'lodash/get';
import reduce from 'lodash/reduce';
import first from 'lodash/first';
import some from 'lodash/some';
import each from 'lodash/each';

import { MAP_URL } from '../constants/regions';
import { DATA_SUCCESS } from '../constants/widgets';
import { makeWidgetVisibleSelector } from '../selectors/widgets';
import { getLocation, rootPageSelector } from '../selectors/global';
import { makePageRoutesByIdSelector } from '../selectors/pages';
import { regionsSelector } from '../selectors/regions';
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

  const tabsRegions = filter(values(regions), region => region.tabs);

  const hasActiveEntity = some(
    tabsRegions,
    region => region.activeEntity !== undefined
  );

  const atLeastOneVisibleWidget = content => {
    return some(content, meta => {
      if (meta.visible === false) {
        return false;
      } else if (meta.content) {
        return atLeastOneVisibleWidget(meta.content);
      } else {
        return makeWidgetVisibleSelector(meta.id);
      }
    });
  };

  const visibleEntities = reduce(
    tabsRegions,
    (acc, region) => {
      const { tabs, regionId } = region;
      let widgetsIds = [];

      each(tabs, tab => {
        const content = get(tab, 'content');

        if (atLeastOneVisibleWidget(content)) {
          widgetsIds.push(tab.id);
        }
      });

      acc.push({ [regionId]: widgetsIds });

      return acc;
    },
    []
  );

  for (let index = 0; index <= visibleEntities.length - 1; index += 1) {
    const visibleEntity = visibleEntities[index];

    if (!hasActiveEntity) {
      for (let index = 0; index <= tabsRegions.length - 1; index += 1) {
        const { regionId } = tabsRegions[index];
        const activeEntity = first(visibleEntity[regionId]);

        yield put(setActiveEntity(regionId, activeEntity));
      }
    }
  }
  mapUrl();
}

export default [takeEvery(MAP_URL, mapUrl), takeEvery(DATA_SUCCESS, switchTab)];
