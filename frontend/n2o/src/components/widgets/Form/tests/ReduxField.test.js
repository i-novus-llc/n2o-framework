import React from 'react';
import { PureReduxField } from '../ReduxField';
import { DEPENDENCY_TYPES } from '../../../../core/dependencyTypes';
import * as observe from '../../../../utils/observeStore';

describe('Проверка ReduxField', () => {
  it('Создается сабскрайб при type = reRender', () => {
    observe.default = jest.fn(() => 'subscribe');
    expect(
      PureReduxField.observeState(
        {},
        {
          dependency: [
            {
              type: DEPENDENCY_TYPES.RE_RENDER
            }
          ]
        },
        () => {}
      )
    ).toEqual('subscribe');
  });
  it('Сабскрайб не создается без type = reRender', () => {
    expect(
      PureReduxField.observeState(
        {},
        {
          dependency: [
            {
              type: 'other type'
            }
          ]
        },
        () => {}
      )
    ).toEqual(undefined);
  });
});
