import React from 'react';
import PropTypes from 'prop-types';
import { Badge } from 'reactstrap';
import { get } from 'lodash';

import Text from '../../../../snippets/Text/Text';

/**
 * Ячейка таблицы типа бейдж
 * @reactProps {string} id
 * @reactProps {object} model - модель данных
 * @reactProps {string} fieldKey - ключ модели для этой ячейки
 * @reactProps {string} text - текст бейджа
 * @reactProps {string} placement - положение бейджа("left" или "right")
 * @reactProps {string} color - цветовая схема бейджа(["default", "danger", "success", "warning", "info")
 * @example
 * <BadgeCell model={model} filedKey={'name'} text="info"/>
 */
class BadgeCell extends React.Component {
  /**
   * Базовый рендер
   */
  render() {
    const { id, model, fieldKey, placement, text, color } = this.props;
    const style = {
      display: 'flex'
    };
    const badgeStyle = {
      order: placement === 'right' ? 1 : -1,
      marginLeft: placement === 'right' && 5,
      marginRight: placement === 'left' && 5
    };
    return (
      <div style={style}>
        <Text text={text} />
        <Badge style={badgeStyle} color={color}>
          {get(model, fieldKey || id)}
        </Badge>
      </div>
    );
  }
}

BadgeCell.propTypes = {
  id: PropTypes.string,
  fieldKey: PropTypes.string,
  model: PropTypes.object,
  placement: PropTypes.oneOf(['left', 'right']),
  text: PropTypes.string,
  color: PropTypes.oneOf(['secondary', 'primary', 'danger', 'success', 'warning', 'info'])
};

BadgeCell.defaultProps = {
  model: {},
  color: 'secondary',
  placement: 'right'
};

export default BadgeCell;
