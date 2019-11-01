import React from 'react';
import PropTypes from 'prop-types';
import { Badge } from 'reactstrap';
import get from 'lodash/get';
import isNil from 'lodash/isNil';

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
  /**
   * ID ячейки
   */
  id: PropTypes.string,
  /**
   * Ключ значения в данных
   */
  fieldKey: PropTypes.string,
  /**
   * Модель данных
   */
  model: PropTypes.object,
  /**
   * Расположение текста
   */
  placement: PropTypes.oneOf(['left', 'right']),
  /**
   * Текст
   */
  text: PropTypes.string,
  /**
   * Формат
   */
  format: PropTypes.string,
  /**
   * Формат баджа
   */
  badgeFormat: PropTypes.string,
  /**
   * Цвет баджа
   */
  color: PropTypes.oneOf([
    'secondary',
    'primary',
    'danger',
    'success',
    'warning',
    'info',
  ]),
  /**
   * Флаг видимости
   */
  visible: PropTypes.bool,
};

BadgeCell.defaultProps = {
  model: {},
  color: 'secondary',
  placement: 'right',
  visible: true,
};

export default BadgeCell;
