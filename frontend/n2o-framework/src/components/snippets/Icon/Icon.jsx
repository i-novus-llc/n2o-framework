import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';

/**
 * Компонент иконки
 * @reactProps {string} className - имя css класса блока
 * @reactProps {object} style - css стили блока
 * @reactProps {boolean} disabled - флаг неактивности иконки
 * @reactProps {string} name - класс иконки
 * @reactProps {boolean} spin - флаг вращения иконки
 * @reactProps {boolean} bordered - флаг рамки вокруг иконки
 * @reactProps {boolean} circular - флаг закругления вокруг иконки
 */

class Icon extends React.Component {
  /**
   * Рендер
   */

  render() {
    const iconClass = cx({
      'n2o-icon': true,
      [`${this.props.name}`]: this.props.name,
      [`${this.props.className}`]: this.props.className,
      disabled: this.props.disabled,
      'fa-spin': this.props.spin,
      circular: this.props.circular,
      bordered: this.props.bordered,
    });

    return (
      <i
        className={iconClass}
        style={{
          ...this.props.style,
        }}
      />
    );
  }
}

Icon.propTypes = {
  className: PropTypes.string,
  style: PropTypes.object,
  disabled: PropTypes.bool,
  name: PropTypes.string.isRequired,
  spin: PropTypes.bool,
  bordered: PropTypes.bool,
  circular: PropTypes.bool,
};

Icon.defaultProps = {
  disabled: false,
  spin: false,
  bordered: false,
  circular: false,
};

export default Icon;
