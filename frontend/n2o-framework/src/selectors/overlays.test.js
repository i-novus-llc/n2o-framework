import { overlaysSelector, makeOverlaysbyName } from './overlays';

const state = {
  overlays: [
    {
      some: 'overlay',
    },
  ],
};

describe('Проверка селекторов overlays', () => {
  it('overlaysSelector должен вернуть overlay', () => {
    expect(overlaysSelector(state)).toEqual(state.overlays);
  });
  it('makeOverlaysByName должен вернуть оверлей по индексу', () => {
    expect(makeOverlaysbyName(0)(state)).toEqual(state.overlays[0]);
  });
});
