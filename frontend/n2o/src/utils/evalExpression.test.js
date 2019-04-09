import evalExpression, { parseExpression } from './evalExpression';

describe('Тесты evalExpression', () => {
  it('parseExpression вернет expression', () => {
    expect(parseExpression('`testExpression`')).toEqual('testExpression');
    expect(parseExpression("`'Имя:' + name`")).toEqual("'Имя:' + name");
  });

  it('parseExpression вернет false', () => {
    expect(parseExpression(0)).toEqual(false);
    expect(parseExpression('`not valid expression')).toEqual(false);
  });

  it('evalExpression вернет expression', () => {
    expect(
      evalExpression(parseExpression('`a + b`'), {
        a: 3,
        b: 4,
      })
    ).toEqual(7);
  });

  it('evalExpression вызовет исключение', () => {
    expect(
      evalExpression(parseExpression('`unknownValue`'), {
        value: 'VALUE',
      })
    ).toEqual(undefined);
  });
});
