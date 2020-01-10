import React from "react";
import PropTypes from "prop-types";
import cx from "classnames";
import { map } from "lodash";

import "./Rating.scss";

/**
 * Контрол для отображения рейтинга в звездах
 */
class RatingControl extends React.Component {
  /**
   * Состояние по-умолчанию
   * @type {{rate: number, value: number}}
   */
  state = {
    rate: 0,
    value: 0,
    prevValue: 0
  };

  /**
   * Пробрасывание value из props в state,
   * если они не равны.
   * @param props
   * @param state
   * @returns {*}
   */
  static getDerivedStateFromProps(props, state) {
    if (props.value !== state.prevValue) {
      return {
        value: props.value,
        prevValue: props.value
      };
    }
    return null;
  }

  /**
   * Обработка наведения мышки
   * @param rate
   */
  handleOver = rate => {
    this.setState(() => ({
      rate
    }));
  };

  /**
   * Обработка мышки, если она вышла за пределы контрола
   */
  handleOut = () => {
    this.setState(() => ({
      rate: this.state.value
    }));
  };

  /**
   * Обработка клика по звездочке и сохранение нового value
   * @param rate
   */
  handleClick = rate => {
    this.setState(
      () => ({
        rate,
        value: rate
      }),
      () => this.props.onChange(rate)
    );
  };

  /**
   * Рендер компонента
   */
  render() {
    const { rate } = this.state;
    return (
      <div className="rating-control" onMouseLeave={this.handleOut}>
        {map(new Array(5), (v, idx) => (
          <i
            key={`rate-${idx}`}
            className={cx(
              idx <= rate
                ? "fa fa-star rating-control-star_active"
                : "fa fa-star-o",
              "rating-control-star"
            )}
            onClick={() => this.handleClick(idx)}
            onMouseEnter={() => this.handleOver(idx)}
          />
        ))}
      </div>
    );
  }
}

RatingControl.propTypes = {
  /**
   * значение
   */
  value: PropTypes.number,
  /**
   * Обработчик изменений
   */
  onChange: PropTypes.func
};

RatingControl.defaultProps = {
  onChange: () => {}
};

export default RatingControl;
