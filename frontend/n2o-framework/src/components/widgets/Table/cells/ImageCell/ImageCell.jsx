import React from 'react';
import PropTypes from 'prop-types';
import { compose, setDisplayName, withHandlers, withState } from 'recompose';
import withCell from '../../withCell';
import imageShapes from './imageShapes';
import get from 'lodash/get';
import withTooltip from '../../withTooltip';

/**
 * Ячейка таблицы с картинкой
 * @reactProps {string} id - id ячейки
 * @reactProps {object} model - модель строки
 * @reactProps {string} shape - тип формы изображения
 * @reactProps {object} style - стили ячейки
 * @reactProps {string} className - имя класса для ячейки
 * @reactProps {string} title - подсказка для картинки
 */

function ImageCell(props) {
  const ref = React.createRef();

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
    width,
    setSize,
    size,
  } = props;

  const setCursor = action => {
    return action ? { cursor: 'pointer' } : null;
  };

  const getSize = () => {
    const height = get(ref, 'current.clientHeight');
    const width = get(ref, 'current.clientWidth');
    if (height && width) setSize(Math.min(height, width));
  };

  const getImageClass = shape => {
    const shapeToClass = {
      rounded: 'rounded',
      thumbnail: 'img-thumbnail',
    };

    return shape ? shapeToClass[shape] : '';
  };

  const setRoundImage = () => {
    return size ? { clipPath: `circle(${size / 2}px at center)` } : {};
  };

  return (
    visible && (
      <span
        title={title}
        style={{ ...style, ...setCursor(action), ...setRoundImage() }}
        className={className}
      >
        <img
          style={{ maxWidth: width }}
          src={get(model, fieldKey || id)}
          alt={title}
          className={getImageClass(shape)}
          onClick={onClick}
          ref={ref}
          onLoad={shape === 'circle' ? getSize : null}
        />
      </span>
    )
  );
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
  width: 'auto',
};

export { ImageCell };
export default compose(
  setDisplayName('ImageCell'),
  withState('size', 'setSize', null),
  withCell,
  withTooltip,
  withHandlers({
    onClick: ({ callAction, model }) => () => {
      callAction(model);
    },
  })
)(ImageCell);
