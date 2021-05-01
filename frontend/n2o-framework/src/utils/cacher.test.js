import cachingStore from './cacher';

describe('Проверка функции cacher', () => {
  it('добавляет и находит в кеше', () => {
    cachingStore.add(
      {
        params: 'any params',
      },
      {
        data: 'cached data',
      }
    );
    expect(
      cachingStore.find({
        params: 'any params',
      })
    ).toEqual({
      data: 'cached data',
    });
  });

  it('вернет false если на найдет значение в кеше', () => {
    cachingStore.add(
      {
        anotherParams: '...params',
      },
      {
        anotherData: '...data',
      }
    );
    expect(
      cachingStore.find({
        unknownParams: '...unknownParams',
      })
    ).toEqual(false);
  });
});
