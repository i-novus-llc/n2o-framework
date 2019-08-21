import { INSERT, DESTROY, HIDE, SHOW } from '../constants/overlays';
import overlays from './overlays';

describe('Тесты overlays reducer', () => {
  it('Проверка INSERT', () => {
    expect(
      overlays(
        [
          {
            name: 'stateModal',
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
        name: 'stateModal',
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
            name: 'stateModal',
            modal: {},
            visible: false,
          },
        ],
        {
          type: SHOW,
          payload: {
            name: 'stateModal',
          },
        }
      )
    ).toEqual([
      {
        name: 'stateModal',
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
            name: 'stateModal',
            modal: {},
            visible: true,
          },
        ],
        {
          type: HIDE,
          payload: {
            name: 'stateModal',
          },
        }
      )
    ).toEqual([
      {
        name: 'stateModal',
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
              name: 'stateModal',
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
