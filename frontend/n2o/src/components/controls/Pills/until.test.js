import { prepareValue, convertData } from './utils';

describe('pills utils test', () => {
  describe('prepareValue', () => {
    it('Указаны все параметры multiSelect=false', () => {
      expect(prepareValue([1], 2)).toEqual([2]);
    });
    it('Указаны все параметры multiSelect=true', () => {
      expect(prepareValue([1], 2, { multiSelect: true })).toEqual([1, 2]);
    });
    it('Должен удалить параметр multiSelect=true', () => {
      expect(prepareValue([1, 2], 2, { multiSelect: true })).toEqual([1]);
    });
  });

  describe('convertData', () => {
    it('Указаны все параметры кроме value', () => {
      expect(
        convertData([], [{ l: 'text', v: 1 }, { l: 'text2', v: 2 }], {
          labelFieldId: 'l',
          valueFieldId: 'v',
        })
      ).toEqual([
        { active: false, id: 1, label: 'text' },
        { active: false, id: 2, label: 'text2' },
      ]);
    });
    it('Указаны все параметры кроме data', () => {
      expect(
        convertData([1], [], {
          labelFieldId: 'l',
          valueFieldId: 'v',
        })
      ).toEqual([]);
    });
    it('Указаны все параметры', () => {
      expect(
        convertData([1], [{ l: 'text', v: 1 }, { l: 'text2', v: 2 }], {
          labelFieldId: 'l',
          valueFieldId: 'v',
        })
      ).toEqual([
        { active: true, id: 1, label: 'text' },
        { active: false, id: 2, label: 'text2' },
      ]);
    });
  });
});
