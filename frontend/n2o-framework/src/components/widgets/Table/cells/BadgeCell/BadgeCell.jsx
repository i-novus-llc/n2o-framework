import React from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import isNil from 'lodash/isNil';
import Badge from 'reactstrap/lib/Badge';

import Text from '../../../../snippets/Text/Text';

/**
 * Ячейка таблицы типа бейдж
 * @reactProps {string} id
 * @reactProps {object} model - модель данных
 * @reactProps {string} fieldKey - ключ модели для этой ячейки
 * @reactProps {string} text - текст бейджа
 * @reactProps {string} placement - положение бейджа("left" или "right")
 * @reactProps {string} color - цветовая схема бейджа(["default", "danger", "success", "warning", "info")
 * @reactProps {bool} badgeIsStatus - флаг отрисовки баджа как статус
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
      badgeIsStatus,
    } = this.props;
    const style = {
      display: 'flex',
    };
    const badgeStyle = {
      order: placement === 'right' ? 1 : -1,
      marginLeft: placement === 'right' && 5,
      marginRight: placement === 'left' && 5,
    };

    const badgeStatusStyle = {
      width: '10px',
      height: '10px',
      margin: '8px 15px 0 0',
    };

    const badgeText = get(model, fieldKey || id);
    return (
      visible && (
        <div style={style}>
          <Text text={text} format={format} />
          {!isNil(badgeText) && (
            <Badge
              style={
                badgeIsStatus
                  ? Object.assign(badgeStyle, badgeStatusStyle)
                  : badgeStyle
              }
              color={color}
            >
              {badgeIsStatus ? (
                ' '
              ) : (
                <Text text={get(model, fieldKey || id)} format={badgeFormat} />
              )}
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
  /**
   * Флаг отрисовки баджа как статус
   */
  badgeIsStatus: PropTypes.bool,
};

BadgeCell.defaultProps = {
  model: {},
  color: 'secondary',
  placement: 'right',
  visible: true,
  badgeIsStatus: false,
};

export default BadgeCell;
