import {
  regionsSelector,
  makeRegionActiveEntitySelector,
  makeRegionByIdSelector,
  makeRegionIsInitSelector,
} from './regions';

const regionId = 'someRegion';
const widgetId = 'someWidget';

const state = {
  regions: {
    someRegion: {
      regionId: regionId,
      isInit: true,
      activeEntity: widgetId,
    },
  },
};

describe('тесты regions selectors', () => {
  it('тест regionsSelector', () => {
    expect(regionsSelector(state)).toEqual(state.regions);
  });
  it('тест makeRegionActiveEntitySelector', () => {
    expect(makeRegionActiveEntitySelector(regionId)(state)).toEqual(
      state.regions[regionId].activeEntity
    );
  });
  it('тест makeRegionByIdSelector', () => {
    expect(makeRegionByIdSelector(regionId)(state)).toEqual(
      state.regions[regionId]
    );
  });
  it('тест makeRegionIsInitSelector', () => {
    expect(makeRegionIsInitSelector(regionId)(state)).toBeTruthy();
  });
});
