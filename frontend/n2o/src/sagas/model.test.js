import { resolveButton, resolveConditions } from './model';
import { CHANGE_BUTTON_DISABLED, CHANGE_BUTTON_VISIBILITY } from '../constants/toolbar';

const setupResolveButton = () => {
  return resolveButton({
    conditions: {
      visible: [
        {
          expression: "test === 'test'",
          modelLink: 'model'
        }
      ],
      enabled: [
        {
          expression: "test !== 'test'",
          modelLink: 'model'
        }
      ]
    }
  });
};

describe('Тестирование саги', () => {
  it('Тестирование вызова  экшена на саге', () => {
    const gen = setupResolveButton();
    gen.next();
    let { value } = gen.next({ model: { test: 'test' } });
    expect(value['PUT'].action.type).toEqual(CHANGE_BUTTON_VISIBILITY);
    expect(value['PUT'].action.payload.visible).toBe(true);
    value = gen.next().value;
    expect(value['PUT'].action.type).toEqual(CHANGE_BUTTON_DISABLED);
    expect(value['PUT'].action.payload.disabled).toBe(true);
    expect(gen.next().done).toBe(true);
  });
  it('Тестирование resolveConditions', () => {
    expect(
      resolveConditions(
        [
          {
            expression: "test === 'test'",
            modelLink: 'model'
          }
        ],
        { model: { test: 'test' } }
      )
    ).toBe(true);
    expect(
      resolveConditions(
        [
          {
            expression: "test === 'test'",
            modelLink: 'no_model'
          }
        ],
        { model: { test: 'test' } }
      )
    ).toBe(false);
  });
  it('Тестирование resolveConditions на null condition', () => {
    expect(
      resolveConditions(
        [
          {
            expression: "test === 'test'",
            modelLink: 'model'
          }
        ],
        null
      )
    ).toBe(false);
  });
});
