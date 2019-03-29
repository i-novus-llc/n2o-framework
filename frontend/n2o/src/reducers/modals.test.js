import { INSERT, DESTROY, HIDE, SHOW } from '../constants/modals';
import modals from './modals';

describe('Тесты modals reducer', () => {
  it('Проверка INSERT', () => {
    expect(
      modals(
        [
          {
            name: 'stateModal'
          }
        ],
        {
          type: INSERT,
          payload: {
            visible: true,
            name: 'testName'
          }
        }
      )
    ).toEqual([
      {
        name: 'stateModal'
      },
      {
        name: 'testName',
        props: {},
        visible: true
      }
    ]);
  });

  it('Проверка SHOW', () => {
    expect(
      modals(
        [
          {
            modal: {
              name: 'stateModal'
            },
            visible: false
          }
        ],
        {
          type: SHOW,
          payload: {
            name: 'stateModal'
          }
        }
      )
    ).toEqual([
      {
        modal: {
          name: 'stateModal'
        },
        visible: true
      }
    ]);
  });

  it('Проверка HIDE', () => {
    expect(
      modals(
        [
          {
            modal: {
              name: 'stateModal'
            },
            visible: true
          }
        ],
        {
          type: HIDE,
          payload: {
            name: 'stateModal'
          }
        }
      )
    ).toEqual([
      {
        modal: {
          name: 'stateModal'
        },
        visible: false
      }
    ]);
  });

  it('Проверка DESTROY', () => {
    expect(
      modals(
        [
          {
            modal: {
              name: 'stateModal'
            },
            visible: true
          }
        ],
        {
          type: DESTROY
        }
      )
    ).toEqual([]);
  });
});
