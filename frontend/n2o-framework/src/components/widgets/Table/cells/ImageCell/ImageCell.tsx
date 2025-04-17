import React, { useCallback } from 'react'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'
import omit from 'lodash/omit'
import flowRight from 'lodash/flowRight'
import classNames from 'classnames'
import { ImageInfo } from '@i-novus/n2o-components/lib/display/Image/ImageInfo'
import { Image } from '@i-novus/n2o-components/lib/display/Image/Image'

import { useResolved } from '../../../../../core/Expression/useResolver'
import { WithCell } from '../../withCell'
import { withTooltip } from '../../withTooltip'
import { ActionWrapper } from '../../../../buttons/StandardButton/ActionWrapper'
import { DefaultCell } from '../DefaultCell'
import { EMPTY_ARRAY } from '../../../../../utils/emptyTypes'

import { ImageStatuses } from './ImageStatuses'
import { type ImageCellProps } from './types'

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

function ImageCellBody({
    title,
    fieldKey,
    style,
    className,
    model,
    id,
    action,
    shape,
    visible,
    disabled,
    description,
    textPosition,
    width,
    height,
    data,
    pathMapping,
    queryMapping,
    target,
    url,
    forwardedRef,
    callAction,
    statuses = EMPTY_ARRAY,
}: ImageCellProps) {
    const onClick = useCallback(() => {
        if (callAction && model) {
            callAction(model)
        }
    }, [callAction, model])
    const src = get(model, fieldKey)

    const hasStatuses = !isEmpty(statuses)
    const hasInfo = title || description

    const defaultImageProps = {
        url: src,
        data,
        title,
        description,
    }

    const resolvedProps = useResolved(defaultImageProps, model)

    const wrapperProps = {
        url,
        target,
        queryMapping,
        pathMapping,
        action,
        className: classNames('n2o-image-cell', { 'with-statuses': hasStatuses }),
    }

    return (
        <DefaultCell
            forwardedRef={forwardedRef}
            tag="div"
            disabled={disabled}
            className={classNames(
                'n2o-image-cell-container',
                {
                    'with-statuses': hasStatuses,
                },
            )
            }
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
                    {...omit(resolvedProps, ['title', 'description'])}
                    // @ts-ignore FIXME разобраться при чем тут resolvedProps.data Array type
                    src={resolvedProps.data || resolvedProps.url}
                />
                {hasStatuses && (
                    <div className="n2o-image-statuses">
                        <ImageStatuses
                            statuses={statuses}
                            model={model}
                            className="image-cell-statuses"
                            onClick={onClick}
                        />
                    </div>
                )}
            </ActionWrapper>
            {hasInfo && (
                <ActionWrapper {...wrapperProps}>
                    <ImageInfo title={title} description={description || ''} />
                </ActionWrapper>
            )}
        </DefaultCell>
    )
}

const ImageCell = flowRight(WithCell, withTooltip)(ImageCellBody)

ImageCell.displayName = 'ImageCell'

export { ImageCell }
export default ImageCell
