import { modalsSelector, makeModalsbyName } from './modals';

const state = {
  modals: [
    {
      some: 'modal'
    }
  ]
};

describe('Проверка селекторов modals', () => {
  it('modalsSelector должен вернуть modals', () => {
    expect(modalsSelector(state)).toEqual(state.modals);
  });
  it('makeModalsByName должен вернуть модальное окно по индексу', () => {
    expect(makeModalsbyName(0)(state)).toEqual(state.modals[0]);
  });
});
