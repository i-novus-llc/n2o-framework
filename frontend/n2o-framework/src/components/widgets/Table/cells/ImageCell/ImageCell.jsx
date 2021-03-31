import React from 'react';
import PropTypes from 'prop-types';
import { compose, setDisplayName, withHandlers } from 'recompose';
import get from 'lodash/get';
import isEmpty from 'lodash/isEmpty';
import omit from 'lodash/omit';
import classNames from 'classnames';

import propsResolver from '../../../../../utils/propsResolver';

import Image from '../../../../snippets/Image/Image';
import ImageInfo from '../../../../snippets/Image/ImageInfo';

import withCell from '../../withCell';
import withTooltip from '../../withTooltip';

import ImageStatuses from './ImageStatuses';
import imageShapes from './imageShapes';

/**
 * Ячейка таблицы с картинкой
 * @reactProps {string} id - id ячейки
 * @reactProps {object} model - модель строки
 * @reactProps {string} shape - тип формы изображения
 * @reactProps {object} style - стили ячейки
 * @reactProps {string} className - имя класса для ячейки
 * @reactProps {string} title - подсказка для картинки
 * @reactProps {string} description - описание
 * @reactProps {string} textPosition - позиция текста
 * @reactProps {string} width - ширина
 * @reactProps {array} statuses - статусы, отображающиеся над img
 */

function ImageCell(props) {
  const {
    title,
    fieldKey,
    style,
    className,
    model,
    id,
    onClick,
    action,
    shape,
    visible,
    description,
    textPosition,
    width,
    height,
    data,
    statuses = [],
  } = props;

  const setCursor = action => {
    return action ? { cursor: 'pointer' } : null;
  };

  const url = get(model, fieldKey);
  const isEmptyModel = isEmpty(model);

  const hasStatuses = !isEmpty(statuses);
  const hasInfo = title || description;

  const defaultImageProps = {
    url: url,
    data: data,
    title: title,
    description: description,
  };

  const resolveProps = isEmptyModel
    ? defaultImageProps
    : propsResolver(defaultImageProps, model);

  return (
    <span className="n2o-image-cell-container">
      <div
        className={classNames('n2o-image-cell', {
          'with-statuses': hasStatuses,
        })}
      >
        <Image
          id={id}
          visible={visible}
          onClick={onClick}
          shape={shape}
          style={{ ...style, ...setCursor(action) }}
          className={className}
          textPosition={textPosition}
          width={width}
          height={height}
          {...omit(resolveProps, ['title', 'description'])}
          src={resolveProps.data || resolveProps.url}
        />
        {hasStatuses && (
          <ImageStatuses
            statuses={statuses}
            model={model}
            className="image-cell-statuses"
            onClick={onClick}
          />
        )}
      </div>
      {hasInfo && <ImageInfo title={title} description={description} />}
    </span>
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
   * Описание
   */
  description: PropTypes.string,
  /**
   * Флаг видимости
   */
  visible: PropTypes.bool,
  /**
   * Позиция текста
   */
  textPosition: PropTypes.oneOf(['top', 'left', 'bottom', 'right']),
  /**
   * Ширина
   */
  width: PropTypes.string,
  /**
   * Статусы, отображающиеся над img
   */
  statuses: PropTypes.array,
};

export { ImageCell };
export default compose(
  setDisplayName('ImageCell'),
  withCell,
  withHandlers({
    onClick: ({ callAction, model }) => () => {
      if (callAction && model) {
        callAction(model);
      }
    },
  }),
  withTooltip
)(ImageCell);
