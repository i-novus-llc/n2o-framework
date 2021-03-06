import React, { Fragment } from 'react'
import PropTypes from 'prop-types'
import { compose, setDisplayName, withHandlers } from 'recompose'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'
import omit from 'lodash/omit'
import classNames from 'classnames'

import propsResolver from '../../../../../utils/propsResolver'
// eslint-disable-next-line import/no-named-as-default
import Image from '../../../../snippets/Image/Image'
// eslint-disable-next-line import/no-named-as-default
import ImageInfo from '../../../../snippets/Image/ImageInfo'
import withCell from '../../withCell'
import withTooltip from '../../withTooltip'
import withLinkAction from '../../../../buttons/StandardButton/withLinkAction'

import ImageStatuses from './ImageStatuses'
import imageShapes from './imageShapes'

/**
 * Обёртка для ХОКа withLinkAction
 * Необходим т.к. текущая реализация логики открытия завязана на последовательность событий:
 * 1) callAction из withCell, который кладёт данные модели в стор
 * 2) onClick самого withLinkAction, который берёт из стора и формирует по ним ссылку для перехода
 * и разделения параметров, необходимых для отображения картинки и открытия записи
 * @property {object} props
 * @property {string} props.url Шаблон адреса открываемой записи
 * @property {object} props.pathMapping Объект подстановки данных в адрес
 * @property {object} props.queryMapping Объект параметров запроса в адрес
 * @property {'application' | '_blank'} [props.target] Тип открытия записи
 * @property children
 */
export const LinkActionWrapper = withLinkAction(props => React.createElement('div', props))

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

    const setCursor = action => (action ? { cursor: 'pointer' } : null)

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

    let Wrapper = Fragment
    let wrapperProps = {}

    if (url || target) {
        Wrapper = LinkActionWrapper
        wrapperProps = {
            url,
            pathMapping,
            queryMapping,
            target,
            className: classNames('n2o-image-cell', { 'with-statuses': hasStatuses }),
        }
    }

    return (
        <span
            className="n2o-image-cell-container"
            onClick={onClick}
        >
            <Wrapper {...wrapperProps}>
                <Image
                    id={id}
                    visible={visible}
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
            </Wrapper>
            {hasInfo && <ImageInfo title={title} description={description} />}
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
