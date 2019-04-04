import { alertsSelector, makeAlertsByKeySelector } from './alerts';

const state = {
  alerts: {
    widgetKey: [
      {
        id: 'testAlert',
        name: 'testAlert',
      },
      {
        id: 'otherAlert',
        name: 'otherAlert',
      },
    ],
  },
};

describe('Проверка селекторов alerts', () => {
  describe('Проверка alertsSelector', () => {
    it('должен вернуть state', () => {
      expect(alertsSelector(state)).toEqual(state.alerts);
    });
    it('должен вернуть пустой объект', () => {
      expect(alertsSelector({})).toEqual({});
    });
  });

  describe('Проверка makeAlertsByKeySelector', () => {
    it('должен вернуть алерт по ключу', () => {
      expect(makeAlertsByKeySelector('widgetKey')(state)).toEqual(
        state.alerts.widgetKey
      );
    });
  });
});
