import groupData from './groupData';

describe('Проверка groupData', () => {
  it('группирует данные', () => {
    expect(
      groupData(
        [
          {
            id: '1',
            value: 'value 1',
          },
          {
            id: '2',
            value: 'value 2',
          },
          {
            id: '3',
            value: 'value 3',
          },
        ],
        'id'
      )
    ).toEqual({
      '1': [{ id: '1', value: 'value 1' }],
      '2': [{ id: '2', value: 'value 2' }],
      '3': [{ id: '3', value: 'value 3' }],
    });
  });
});
