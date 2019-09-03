import React from 'react';
import cx from 'classnames';
import { omit } from 'lodash';
import BaseSlider, { createSliderWithTooltip } from 'rc-slider';
import { stringConverter, prepareStyle } from './utils';
import { propTypes, defaultProps } from './allProps';
/**
 * Компонент Slider
 * @reactProps {boolean} multiple - Множественный выбор
 * @reactProps {boolean} showTooltip - Показать тултип
 * @reactProps {string} tooltipPlacement - Позиция тултипа
 * @reactProps {string} tooltipFormatter - Форматированный вывод тултипа
 * @reactProps {boolean} vertical - Отобразить slider вертикально
 * @reactProps {boolean} style - Стили root компонента
 * @reactProps {boolean} className - Дополнительный класс
 * @reactProps {string|number} step - шаг ползунка
 * @reactProps {boolean} disabled - Нередактаруем
 * @reactProps {boolean} dots - Показать шкалу
 * @reactProps {number|string} min - Начало шкалы
 * @reactProps {number|string} max - Конец шкалы
 * @reactProps {object} marks - Подписи к шкале
 * @reactProps {boolean} pushable - В мульти режиме блокирует смену несколькох ползунков
 * @reactProps {object} trackStyle - стиль трека
 * @reactProps {object} railStyle - стиль непройденной части шкалы
 * @reactProps {object} dotStyle - стиль шкалы
 * @returns {*}
 * @constructor
 */
function Slider(props) {
  const {
    multiple,
    showTooltip,
    tooltipPlacement,
    tooltipFormatter,
    vertical,
    style,
    className,
    onChange,
    ...rest
  } = props;

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

  const handleAfterChange = value => {
    onChange(value);
  };

  const sliderProps = omit(rest, ['value']);

  return (
    <RenderSlider
      className={cx('n2o-slider', className)}
      tipProps={tooltipProps}
      tipFormatter={expressionFn}
      vertical={vertical}
      style={prepareStyle(vertical, style)}
      onAfterChange={handleAfterChange}
      {...sliderProps}
    />
  );
}

const WrapSlider = stringConverter([
  'value',
  'max',
  'min',
  'step',
  'stoppingValue',
])(Slider);

WrapSlider.propTypes = propTypes;
WrapSlider.defaultProps = defaultProps;

export default WrapSlider;
