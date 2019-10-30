import fetchMock from 'fetch-mock';
import request from './request';

describe('Проверка request', () => {
  it('вернет ответ', async () => {
    fetchMock.get('/n2o/success-test', () => ({
      response: {
        success: true,
      },
    }));

    const response = await request('/n2o/success-test');
    expect(response).toEqual({
      response: {
        success: true,
      },
    });
  });
});
