import React from 'react';
import BaseSlider, { createSliderWithTooltip } from 'rc-slider';
import { stringConverter } from './untils';
import { propTypes, defaultProps } from './allProps';
import 'rc-slider/assets/index.css';

function Slider({
  multiple,
  showTooltip,
  tooltipPlacement,
  tooltipFormatter,
  ...rest
}) {
  const expressionFn = tooltipFormatter
    ? value => new Function('', 'return ' + tooltipFormatter).bind(value)()
    : value => value;

  const Component = multiple ? BaseSlider.Range : BaseSlider;
  const RenderSlider = showTooltip
    ? createSliderWithTooltip(Component)
    : Component;

  const tooltipProps = {
    placement: tooltipPlacement,
  };

  return (
    <RenderSlider
      tipProps={tooltipProps}
      tipFormatter={expressionFn}
      {...rest}
    />
  );
}

const WrapSlider = stringConverter(['value', 'max', 'min', 'step'])(Slider);

WrapSlider.propTypes = propTypes;
WrapSlider.defaultProps = defaultProps;

export default WrapSlider;
