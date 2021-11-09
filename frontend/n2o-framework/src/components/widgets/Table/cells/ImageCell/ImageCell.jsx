import React from 'react'
import PropTypes from 'prop-types'
import { compose, setDisplayName, withHandlers } from 'recompose'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'
import omit from 'lodash/omit'
import classNames from 'classnames'

import propsResolver from '../../../../../utils/propsResolver'
import { Image } from '../../../../snippets/Image/Image'
import { ImageInfo } from '../../../../snippets/Image/ImageInfo'
import withCell from '../../withCell'
import withTooltip from '../../withTooltip'
import { ActionWrapper } from '../../../../buttons/StandardButton/ActionWrapper'

import ImageStatuses from './ImageStatuses'
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
        pathMapping,
        queryMapping,
        target,
        url,
        statuses = [],
    } = props

    const src = get(model, fieldKey)
    const isEmptyModel = isEmpty(model)

    const hasStatuses = !isEmpty(statuses)
    const hasInfo = title || description

    const defaultImageProps = {
        url: src,
        data,
        title,
        description,
    }

    const resolveProps = isEmptyModel
        ? defaultImageProps
        : propsResolver(defaultImageProps, model)

    const wrapperProps = {
        url,
        target,
        queryMapping,
        pathMapping,
        action,
        className: classNames('n2o-image-cell', { 'with-statuses': hasStatuses }),
    }

    return (
        <span
            className="n2o-image-cell-container"
            onClick={onClick}
        >
            <ActionWrapper {...wrapperProps}>
                <Image
                    id={id}
                    visible={visible}
                    shape={shape}
                    style={style}
                    className={classNames(
                        className,
                        {
                            'n2o-image-cell__image-with-action': action || url || target,
                        },
                    )}
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
            </ActionWrapper>
            {hasInfo && (
                <ActionWrapper {...wrapperProps}>
                    <ImageInfo
                        title={title}
                        description={description}
                    />
                </ActionWrapper>
            )}
        </span>
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
    /**
     * Статусы, отображающиеся над img
     */
    statuses: PropTypes.array,
    fieldKey: PropTypes.string,
    onClick: PropTypes.func,
    action: PropTypes.string,
    height: PropTypes.number,
    data: PropTypes.object,
    pathMapping: PropTypes.object,
    queryMapping: PropTypes.object,
    target: PropTypes.string,
    url: PropTypes.string,
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
