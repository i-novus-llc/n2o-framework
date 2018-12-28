import { insertModal, showModal, hideModal, destroyModal } from './modals';
import { INSERT, DESTROY, HIDE, SHOW } from '../constants/modals';

const name = 'MODAL_NAME';

describe('Тесты экшенов modals', () => {
  describe('Проверка экшена insertModal', () => {
    it('Генирирует правильное событие', () => {
      const action = insertModal(name, true, 'TITLE', 'lg', true, 'page_id', 'TableWidget');
      expect(action.type).toEqual(INSERT);
    });
    it('Возвращает правильный payload', () => {
      const action = insertModal(name, true, 'TITLE', 'lg', true, 'page_id', 'TableWidget');
      expect(action.payload.name).toEqual(name);
      expect(action.payload.visible).toEqual(true);
      expect(action.payload.title).toEqual('TITLE');
      expect(action.payload.size).toEqual('lg');
      expect(action.payload.closeButton).toEqual(true);
      expect(action.payload.pageId).toEqual('page_id');
      expect(action.payload.src).toEqual('TableWidget');
    });
  });

  describe('Проверка экшена showModal', () => {
    it('Генирирует правильное событие', () => {
      const action = showModal(name);
      expect(action.type).toEqual(SHOW);
    });
    it('Возвращает правильный payload', () => {
      const action = showModal(name);
      expect(action.payload.name).toEqual(name);
    });
  });

  describe('Проверка экшена hideModal', () => {
    it('Генирирует правильное событие', () => {
      const action = hideModal(name);
      expect(action.type).toEqual(HIDE);
    });
    it('Возвращает правильный payload', () => {
      const action = hideModal(name);
      expect(action.payload.name).toEqual(name);
    });
  });

  describe('Проверка экшена destroyModal', () => {
    it('Генирирует правильное событие', () => {
      const action = destroyModal(name);
      expect(action.type).toEqual(DESTROY);
    });
  });
});
