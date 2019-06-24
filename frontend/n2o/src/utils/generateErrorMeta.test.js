import { generateErrorMeta } from './generateErrorMeta';
import { defaultState } from '../reducers/alerts';

describe('проверка generateErrorMeta', () => {
  it('генерирует объект meta error из ошибки', () => {
    expect(
      generateErrorMeta({
        value: 'any message',
      })
    ).toEqual({
      alert: {
        messages: [
          {
            ...defaultState,
            ...{ value: 'any message' },
          },
        ],
      },
    });
  });

  it('генерирует default meta error, если нет присланной ошибки', () => {
    expect(generateErrorMeta()).toEqual({
      alert: {
        messages: [
          {
            ...defaultState,
          },
        ],
      },
    });
  });
});
