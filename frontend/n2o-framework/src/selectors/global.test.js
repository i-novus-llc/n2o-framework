import {
  globalSelector,
  localeSelector,
  appLoadingSelector,
  localizationSelector,
  menuSelector,
  userSelector,
  errorSelector,
  rootPageSelector,
  routerSelector,
  getLocation,
} from './global';

const state = {
  global: {
    locale: 'ru_RU',
    loading: false,
    messages: [
      {
        some: 'value',
      },
    ],
    menu: {
      some: 'config',
    },
    error: false,
    rootPageId: '_',
  },
  router: {
    router: 'config',
    location: '/n2o',
  },
};

describe('Проверка селекторов global', () => {
  it('globalSelector должен вернуть global', () => {
    expect(globalSelector(state)).toEqual(state.global);
  });
  it('localeSelector должен вернуть locale', () => {
    expect(localeSelector(state)).toEqual(state.global.locale);
  });
  it('appLoadingSelector должен вернуть loading', () => {
    expect(appLoadingSelector(state)).toEqual(state.global.loading);
  });
  it('localizationSelector должен вернуть messages', () => {
    expect(localizationSelector(state)).toEqual(state.global.messages);
  });
  it('menuSelector должен вернуть menu', () => {
    expect(menuSelector(state)).toEqual(state.global.menu);
  });
  it('errorSelector должен вернуть error', () => {
    expect(errorSelector(state)).toEqual(state.global.error);
  });
  it('rootPageSelector должен вернуть rootPageId', () => {
    expect(rootPageSelector(state)).toEqual(state.global.rootPageId);
  });
  it('routerSelector должен вернуть router', () => {
    expect(routerSelector(state)).toEqual(state.router);
  });
  it('getLocation должен вернуть location', () => {
    expect(getLocation(state)).toEqual(state.router.location);
  });
});
