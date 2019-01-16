import React from 'react';
import PropTypes from 'prop-types';
import withCell from '../../withCell';
import imageShapes from './imageShapes';
import { get } from 'lodash';

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
        thumbnail: 'img-thumbnail'
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
      callActionImpl,
      action
    } = this.props;

    return (
      <div title={title} style={{ ...style, ...this.setCursor(action) }} className={className}>
        <img
          src={get(model, fieldKey || id)}
          alt={title}
          className={getImageClass(shape)}
          onClick={callActionImpl}
        />
      </div>
    );
  }
}

ImageCell.propTypes = {
  id: PropTypes.string.isRequired,
  model: PropTypes.object.isRequired,
  shape: PropTypes.oneOf(Object.values(imageShapes)),
  style: PropTypes.object,
  className: PropTypes.string,
  title: PropTypes.string
};

export default withCell(ImageCell);
