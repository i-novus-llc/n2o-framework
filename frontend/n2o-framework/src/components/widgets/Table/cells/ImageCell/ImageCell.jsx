import React from 'react';
import PropTypes from 'prop-types';
import {compose, setDisplayName, withHandlers,} from 'recompose';
import withCell from '../../withCell';
import imageShapes from './imageShapes';
import get from 'lodash/get';
import isEqual from 'lodash/isEqual';
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

class ImageCell extends React.Component {
  constructor(props) {
    super(props);
    this.ref = React.createRef();
    this.state = {
      size: null,
    };
  }

  componentDidMount() {
    setTimeout(() => {
      const height = get(this.ref, 'current.clientHeight');
      const width = get(this.ref, 'current.clientWidth');
      if (height && width)
        this.setState({ size: Math.min(height, width) }, () =>
          console.warn('cdu >', this.state)
        );
    }, 500);
  }

  componentDidUpdate(prevProps, prevState, snapshot) {
    if (this.ref && !this.state.size) {
      setTimeout(() => {
        this.setState(
          {
            size: Math.min(
              this.ref.current.clientHeight,
              this.ref.current.clientWidth
            ),
          },
          console.warn('cdu >>', this.state)
        );
      }, 500);
    }
  }

  render() {
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
    } = this.props;

    const setCursor = action => {
      return action ? { cursor: 'pointer' } : null;
    };

    const getImageClass = shape => {
      const shapeToClass = {
        rounded: 'rounded',
        thumbnail: 'img-thumbnail',
      };

      return shape ? shapeToClass[shape] : '';
    };

    const setRoundImage = () => {
      return this.state.size
        ? { clipPath: `circle(${this.state.size / 2}px at center)` }
        : {};
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
            ref={this.ref}
          />
        </span>
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
  width: 'auto',
};

export { ImageCell };
export default compose(
  setDisplayName('ImageCell'),
  withCell,
  withTooltip,
  withHandlers({
    onClick: ({ callAction, model }) => () => {
      callAction(model);
    },
  })
)(ImageCell);
