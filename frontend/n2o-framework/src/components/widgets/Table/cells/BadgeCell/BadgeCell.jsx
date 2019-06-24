import React from 'react';
import PropTypes from 'prop-types';
import { Badge } from 'reactstrap';
import { get, isNil } from 'lodash';

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
    const {
      id,
      model,
      fieldKey,
      placement,
      text,
      format,
      badgeFormat,
      color,
      visible,
    } = this.props;
    const style = {
      display: 'flex',
    };
    const badgeStyle = {
      order: placement === 'right' ? 1 : -1,
      marginLeft: placement === 'right' && 5,
      marginRight: placement === 'left' && 5,
    };
    const badgeText = get(model, fieldKey || id);
    return (
      visible && (
        <div style={style}>
          <Text text={text} format={format} />
          {!isNil(badgeText) && (
            <Badge style={badgeStyle} color={color}>
              <Text text={get(model, fieldKey || id)} format={badgeFormat} />
            </Badge>
          )}
        </div>
      )
    );
  }
}

BadgeCell.propTypes = {
  id: PropTypes.string,
  fieldKey: PropTypes.string,
  model: PropTypes.object,
  placement: PropTypes.oneOf(['left', 'right']),
  text: PropTypes.string,
  format: PropTypes.string,
  badgeFormat: PropTypes.string,
  color: PropTypes.oneOf([
    'secondary',
    'primary',
    'danger',
    'success',
    'warning',
    'info',
  ]),
  visible: PropTypes.bool,
};

BadgeCell.defaultProps = {
  model: {},
  color: 'secondary',
  placement: 'right',
  visible: true,
};

export default BadgeCell;
