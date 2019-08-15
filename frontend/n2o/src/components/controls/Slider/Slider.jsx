import React from 'react';
import cx from 'classnames';
import { withState, compose } from 'recompose';
import BaseSlider, { Handle } from 'rc-slider';
import { stringConverter, prepareStyle } from './utils';
import { propTypes, defaultProps } from './allProps';
import Tooltip from '../../../utils/withTooltip';
import { id } from '../../../utils/id';

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
    Id,
    ...rest
  } = props;

  const onChange = value => {
    rest.setValue(value);
  };

  const onAfterChange = value => {
    rest.onChange(value);
    console.log(value);
  };

  const expressionFn = tooltipFormatter
    ? value => new Function('', 'return ' + tooltipFormatter).bind(value)()
    : value => value;

  const Component = multiple ? BaseSlider.Range : BaseSlider;

  const renderHandle = rest => {
    console.log(Id);
    if (showTooltip)
      return (
        <Tooltip
          hint={rest.value}
          placement={tooltipPlacement}
          isOpen={rest.dragging}
          id={Id}
        >
          <Handle {...rest} id={Id} />
        </Tooltip>
      );
    else return <Handle {...rest} />;
  };

  return (
    <Component
      {...rest}
      className={cx('n2o-slider', className)}
      handle={renderHandle}
      vertical={vertical}
      style={prepareStyle(vertical, style)}
      onAfterChange={onAfterChange}
      onChange={onChange}
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

const enhance = compose(
  withState('Id', 'setId', () => id()),
  withState('value', 'setValue', ({ value }) => value)
);

export default enhance(WrapSlider);
