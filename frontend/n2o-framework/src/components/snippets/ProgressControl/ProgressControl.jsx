import React from 'react';
import PropTypes from 'prop-types';
import Progress from 'reactstrap/lib/Progress';
import isUndefined from 'lodash/isUndefined';
import isArray from 'lodash/isArray';
import map from 'lodash/map';

function ProgressControl(props) {
  const { multi, className, barClassName, bars, value, label, max } = props;

  const renderProgressControl = () => {
    const multiProgressControl = multi && !isUndefined(bars) && isArray(bars);
    const barsCollection = multiProgressControl && bars;

    //мультирежим ProgressControl
    const renderMultiProgressControl = () => {
      return (
        <Progress multi>
          {map(barsCollection, bar => {
            const { id, label } = bar;
            return (
              <Progress
                bar
                key={id}
                className={barClassName}
                max={max}
                value={!isUndefined(value) && value[id]}
                {...bar}
              >
                {label}
              </Progress>
            );
          })}
        </Progress>
      );
    };

    //одиночный режим ProgressControl
    const renderSimpleProgressControl = () => {
      return <Progress {...props}>{label}</Progress>;
    };

    return (
      <div className={className}>
        {multiProgressControl
          ? renderMultiProgressControl()
          : renderSimpleProgressControl()}
      </div>
    );
  };
  return renderProgressControl();
}

ProgressControl.propTypes = {
  /**
   * флаг мультирежима в Progress
   */
  multi: PropTypes.bool.isRequired,
  /**
   * Массив с Progress-bars в мультирежиме
   */
  bars: PropTypes.array.isRequired,
  /**
   * id Progress
   */
  id: PropTypes.string.isRequired,
  /**
   * Флаг анимированности
   */
  animated: PropTypes.bool,
  /**
   * Флаг штриховки
   */
  striped: PropTypes.bool,
  /**
   * class контейнера
   */
  className: PropTypes.string.isRequired,
  /**
   * class Progress
   */
  barClassName: PropTypes.string.isRequired,
  /**
   * title на Progress
   */
  label: PropTypes.string.isRequired,
  /**
   * значение, на сколько заполнен Progress
   */
  value: PropTypes.number || PropTypes.object,
  /**
   * максиальное значение шкалы
   */
  max: PropTypes.number,
  /**
   * цвет Progress
   */
  color: PropTypes.string,
};

ProgressControl.defaultProps = {
  multi: false,
  animated: false,
  striped: false,
};

export default ProgressControl;
