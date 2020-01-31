import { INSERT, DESTROY, HIDE, SHOW } from '../constants/overlays';
import overlays from './overlays';

describe('Тесты overlays reducer', () => {
  it('Проверка INSERT', () => {
    expect(
      overlays(
        [
          {
            name: 'stateOverlay',
          },
        ],
        {
          type: INSERT,
          payload: {
            visible: true,
            name: 'testName',
          },
        }
      )
    ).toEqual([
      {
        name: 'stateOverlay',
      },
      {
        name: 'testName',
        props: {},
        visible: true,
      },
    ]);
  });

  it('Проверка SHOW', () => {
    expect(
      overlays(
        [
          {
            name: 'stateOverlay',
            modal: {},
            visible: false,
          },
        ],
        {
          type: SHOW,
          payload: {
            name: 'stateOverlay',
          },
        }
      )
    ).toEqual([
      {
        name: 'stateOverlay',
        modal: {},
        visible: true,
      },
    ]);
  });

  it('Проверка HIDE', () => {
    expect(
      overlays(
        [
          {
            name: 'stateOverlay',
            modal: {},
            visible: true,
          },
        ],
        {
          type: HIDE,
          payload: {
            name: 'stateOverlay',
          },
        }
      )
    ).toEqual([
      {
        name: 'stateOverlay',
        modal: {},
        visible: false,
      },
    ]);
  });

  it('Проверка DESTROY', () => {
    expect(
      overlays(
        [
          {
            modal: {
              name: 'stateOverlay',
            },
            visible: true,
          },
        ],
        {
          type: DESTROY,
        }
      )
    ).toEqual([]);
  });
});
