import React from 'react';
import SwitchCell from './SwitchCell';
import {
  model,
  model2,
  modelFromDefaultView,
} from './switchCellStoryProps';

const setupOptions = model => shallow(<SwitchCell model={model} />);

describe('SwitchCell tests <SwitchCell>', () => {
  it('Компонент возвращает верный cell - TextCell', () => {
    const wrapper = setupOptions(model);
    expect(wrapper.props().model.src).toBe('TextCell');
  });

  it('Компонент возвращает верный cell - LinkCell', () => {
    const wrapper = setupOptions(model2);
    expect(wrapper.props().model.src).toBe('LinkCell');
  });

  it('Компонент возвращает верный default cell - ImageCell', () => {
    const wrapper = setupOptions(modelFromDefaultView);
    expect(wrapper.props().model.src).toBe('ImageCell');
  });
});
