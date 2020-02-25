import { registerRegion, setActiveEntity } from '../actions/regions';
import regionReducer from './regions';

const state = {
  existedRegion: {
    isInit: false,
  },
};
const regionId = 'someRegion';
const widgetId = 'someWidget';
const initProps = {
  isInit: true,
  list: [],
  activeEntity: widgetId,
};

describe('тесты regions reducer', () => {
  it('регистрация региона', () => {
    expect(regionReducer(state, registerRegion(regionId, initProps))).toEqual({
      ...state,
      [regionId]: initProps,
    });
  });

  it('изменение активной сущности', () => {
    expect(regionReducer(state, setActiveEntity(regionId, widgetId))).toEqual({
      ...state,
      [regionId]: {
        activeEntity: widgetId,
      },
    });
  });
});
