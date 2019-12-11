import factoryResolver from './factoryResolver';
import NotFoundFactory from '../../core/factory/NotFoundFactory';
import InputText from '../../components/controls/InputText/InputText';

describe('Проверка factoryResolver', () => {
  it('должен вернуть component', () => {
    expect(factoryResolver('InputText')).toEqual(InputText);
    expect(
      factoryResolver({
        src: 'InputText',
        autoFocus: true,
        value: 'test',
      })
    ).toEqual({
      component: InputText,
      autoFocus: true,
      value: 'test',
    });
    expect(
      factoryResolver({
        test: {
          src: 'InputText',
        },
        test2: {
          src: 'InputText',
        },
      })
    ).toEqual({
      test: {
        component: InputText,
      },
      test2: {
        component: InputText,
      },
    });
  });

  it('если props не объект и не строка то вернет props', () => {
    expect(factoryResolver(true)).toEqual(true);
    expect(factoryResolver(['test', 'test2'])).toEqual(['test', 'test2']);
    expect(factoryResolver(undefined)).toEqual(undefined);
    expect(factoryResolver(null)).toEqual(null);
  });

  it('если src не найден, то вернет NotFoundFactory', () => {
    expect(factoryResolver('notFound')).toEqual(NotFoundFactory);
  });
});
