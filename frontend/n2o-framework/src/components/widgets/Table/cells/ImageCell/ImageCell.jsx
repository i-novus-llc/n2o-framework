import React from 'react';
import PropTypes from 'prop-types';
import { compose, setDisplayName, withHandlers } from 'recompose';
import withCell from '../../withCell';
import imageShapes from './imageShapes';
import get from 'lodash/get';

/**
 * Ячейка таблицы с картинкой
 * @reactProps {string} id - id ячейки
 * @reactProps {object} model - модель строки
 * @reactProps {string} shape - тип формы изображения
 * @reactProps {object} style - стили ячейки
 * @reactProps {string} className - имя класса для ячейки
 * @reactProps {string} title - подсказка для картинки
 */

class ImageCell extends React.Component {
  setCursor(action) {
    return action ? { cursor: 'pointer' } : null;
  }

  /**
   * Рендер
   */

  render() {
    const getImageClass = shape => {
      const shapeToClass = {
        rounded: 'rounded',
        circle: 'rounded-circle',
        thumbnail: 'img-thumbnail',
      };

      return shape ? shapeToClass[shape] : '';
    };

    const {
      title,
      fieldKey,
      style,
      className,
      model,
      id,
      shape,
      onClick,
      action,
      visible,
    } = this.props;

    return (
      visible && (
        <div
          title={title}
          style={{ ...style, ...this.setCursor(action) }}
          className={className}
        >
          <img
            src={get(model, fieldKey || id)}
            alt={title}
            className={getImageClass(shape)}
            onClick={onClick}
          />
        </div>
      )
    );
  }
}

ImageCell.propTypes = {
  /**
   * ID ячейки
   */
  id: PropTypes.string.isRequired,
  /**
   * Модель данных
   */
  model: PropTypes.object.isRequired,
  /**
   * Тип формы изображенич
   */
  shape: PropTypes.oneOf(Object.values(imageShapes)),
  /**
   * Стили
   */
  style: PropTypes.object,
  /**
   * Класс
   */
  className: PropTypes.string,
  /**
   * Заголовок
   */
  title: PropTypes.string,
  /**
   * Флаг видимости
   */
  visible: PropTypes.bool,
};

ImageCell.defaultProps = {
  visible: true,
};

export { ImageCell };
export default compose(
  setDisplayName('ImageCell'),
  withCell,
  withHandlers({
    onClick: ({ callAction, model }) => () => {
      callAction(model);
    },
  })
)(ImageCell);
