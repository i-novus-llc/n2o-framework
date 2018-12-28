import linkResolver from './linkResolver';

const config = {
  link: '',
  value: ''
};

const state = {
  a: {
    b: {
      c: 'test'
    }
  }
};

describe('Проверка linkResolver', () => {
  it('пустой конфиг', () => {
    const res = linkResolver(state, {});
    expect(res).toBe(undefined);
  });
  it('только link', () => {
    const res = linkResolver(state, {
      link: 'a.b.c'
    });
    expect(res).toBe('test');
  });
  it('только link - кривой путь', () => {
    const res = linkResolver(state, {
      link: 'q.w.e'
    });
    expect(res).toBe(undefined);
  });
  it('только value - константа', () => {
    const res = linkResolver(state, {
      value: 1
    });
    expect(res).toBe(1);
  });
  it('только value - js expression', () => {
    const res = linkResolver(state, {
      value: '`2+2`'
    });
    expect(res).toBe(4);
  });
  it('value и link', () => {
    const res = linkResolver(state, {
      link: 'a.b.c',
      value: '`this+"-n2o"`'
    });
    expect(res).toBe('test-n2o');
  });
  it('value (константа) и link', () => {
    const res = linkResolver(state, {
      link: 'a.b.c',
      value: '123'
    });
    expect(res).toBe('123');
  });
  it('value и link (кривой)', () => {
    const res = linkResolver(state, {
      link: 'q.w.e',
      value: '`this.x`'
    });
    expect(res).toBe(undefined);
  });
  it('value (this) и link (кривой)', () => {
    const res = linkResolver(state, {
      link: 'q.w.e',
      value: '`this`'
    });
    expect(res).toEqual({});
  });
});
