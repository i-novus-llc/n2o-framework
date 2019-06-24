import React from 'react';
import PropTypes from 'prop-types';
import { Progress } from 'reactstrap';

import progressBarStyles from './progressBarStyles';

/**
 * Ячейка таблицы с прогресс баром
 * @reactProps {string} id - id ячейки
 * @reactProps {object} model - модель строки
 * @reactProps {boolean} animated - флаг animated
 * @reactProps {boolean} striped - флаг striped
 * @reactProps {string} color - цвет прогресс бара
 * @reactProps {string} size - размер прогресс бара
 */

class ProgressBarCell extends React.Component {
  /**
   * Рендер
   */

  render() {
    const { id, animated, striped, color, size, visible } = this.props;

    return (
      visible && (
        <Progress
          value={this.props.model[id]}
          className={size}
          animated={animated}
          striped={striped}
          color={color}
        />
      )
    );
  }
}

ProgressBarCell.propTypes = {
  id: PropTypes.string.isRequired,
  model: PropTypes.object.isRequired,
  animated: PropTypes.bool,
  striped: PropTypes.bool,
  color: PropTypes.oneOf(Object.values(progressBarStyles)),
  size: PropTypes.oneOf(['mini', 'default', 'large']),
  visible: PropTypes.bool,
};

ProgressBarCell.defaultProps = {
  animated: false,
  striped: false,
  size: 'default',
  visible: true,
};

export default ProgressBarCell;
