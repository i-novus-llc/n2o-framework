import React from 'react'
import PropTypes from 'prop-types'
import { compose, setDisplayName, withHandlers } from 'recompose'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'

import propsResolver from '../../../../../utils/propsResolver'
import withCell from '../../withCell'
import withTooltip from '../../withTooltip'
import Image from '../../../../snippets/Image/Image'

import imageShapes from './imageShapes'

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
        data,
    } = props

    const setCursor = action => (action ? { cursor: 'pointer' } : null)

    const url = get(model, fieldKey)
    const isEmptyModel = isEmpty(model)

    const defaultImageProps = {
        url,
        data,
        title,
        description,
    }

    const resolveProps = isEmptyModel
        ? defaultImageProps
        : propsResolver(defaultImageProps, model)

    return (
        <Image
            id={id}
            visible={visible}
            title={title}
            description={description}
            onClick={onClick}
            shape={shape}
            style={{ ...style, ...setCursor(action) }}
            className={className}
            textPosition={textPosition}
            width={width}
            {...resolveProps}
            src={resolveProps.data || resolveProps.url}
        />
    )
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
}

export { ImageCell }
export default compose(
    setDisplayName('ImageCell'),
    withCell,
    withHandlers({
        onClick: ({ callAction, model }) => () => {
            if (callAction && model) {
                callAction(model)
            }
        },
    }),
    withTooltip,
)(ImageCell)
