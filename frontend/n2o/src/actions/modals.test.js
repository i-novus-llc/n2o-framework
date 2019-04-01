import {
  insertModal,
  showModal,
  hideModal,
  destroyModal,
  closeModal,
  hidePrompt,
  showPrompt,
} from './modals';
import {
  INSERT,
  DESTROY,
  HIDE,
  SHOW,
  CLOSE,
  SHOW_PROMPT,
  HIDE_PROMPT,
} from '../constants/modals';

const name = 'MODAL_NAME';

describe('Тесты экшенов modals', () => {
  describe('Проверка экшена insertModal', () => {
    it('Генирирует правильное событие', () => {
      const action = insertModal(
        name,
        true,
        'TITLE',
        'lg',
        true,
        'page_id',
        'TableWidget'
      );
      expect(action.type).toEqual(INSERT);
    });
    it('Возвращает правильный payload', () => {
      const action = insertModal(
        name,
        true,
        'TITLE',
        'lg',
        true,
        'page_id',
        'TableWidget'
      );
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
      const action = destroyModal();
      expect(action.type).toEqual(DESTROY);
    });
  });

  describe('Проверка экшена closeModal', () => {
    it('Генирирует правильное событие', () => {
      const action = closeModal('test', true);
      expect(action.type).toEqual(CLOSE);
    });
    it('Возвращает правильный payload', () => {
      const action = closeModal('test', true);
      expect(action.payload).toEqual({
        name: 'test',
        prompt: true,
      });
    });
  });

  describe('Проверка экшена showPrompt', () => {
    it('Генирирует правильное событие', () => {
      const action = showPrompt('test', true);
      expect(action.type).toEqual(SHOW_PROMPT);
    });
    it('Возвращает правильный payload', () => {
      const action = showPrompt('test');
      expect(action.payload).toEqual({
        name: 'test',
      });
    });
  });

  describe('Проверка экшена hidePrompt', () => {
    it('Генирирует правильное событие', () => {
      const action = hidePrompt('test', true);
      expect(action.type).toEqual(HIDE_PROMPT);
    });
    it('Возвращает правильный payload', () => {
      const action = hidePrompt('test');
      expect(action.payload).toEqual({
        name: 'test',
      });
    });
  });
});
