import React from 'react'
import { connect } from 'react-redux'
import classNames from 'classnames'
import isEmpty from 'lodash/isEmpty'
import omit from 'lodash/omit'
import { ImageInfo } from '@i-novus/n2o-components/lib/display/Image/ImageInfo'
import { Image } from '@i-novus/n2o-components/lib/display/Image/Image'

import { ImageStatuses } from '../../../Table/cells/ImageCell/ImageStatuses'
import { ActionWrapper } from '../../../../buttons/StandardButton/ActionWrapper'
import { useResolved } from '../../../../../core/Expression/useResolver'
import { State } from '../../../../../ducks/State'

import { type Props } from './types'

/**
 * Компонент Image фомы
 * @reactProps {string} id - id
 * @reactProps {string} url - ссылка на img
 * @reactProps {string} data - img в байтах на сервере
 * @reactProps {string} title - title рядом с img
 * @reactProps {string} description - description рядом с img
 * @reactProps {string} textPosition - позиция текста рядом с img (top || left || bottom || right = default )
 * @reactProps {string} - width - кастом ширина
 * @reactProps {string} shape - форма маски img (square || circle || rounded = default)
 * @reactProps {boolean} visible - флаг видимости сниппета
 * @reactProps {array} statuses - статусы, отображающиеся над img
 */

function ImageFieldBody({
    id,
    url,
    data,
    title,
    description,
    textPosition,
    width,
    height,
    shape,
    visible,
    model,
    className,
    pathMapping,
    queryMapping,
    target,
    action,
    statuses,
}: Props) {
    const hasStatuses = !isEmpty(statuses)

    const defaultImageProps = { url, data, title, description }
    const resolvedProps = useResolved(defaultImageProps, model)

    return (
        <div
            className={classNames('n2o-image-field-container', {
                [textPosition]: textPosition,
            })}
        >
            <ActionWrapper
                url={url}
                target={target}
                pathMapping={pathMapping}
                queryMapping={queryMapping}
                action={action}
                className="n2o-image-field__image"
            >
                <div className={classNames('n2o-image-field', { 'with-statuses': hasStatuses })}>
                    <Image
                        id={id}
                        visible={visible}
                        shape={shape}
                        className={classNames(
                            className,
                            {
                                'n2o-image-field__image-with-action': action || url || target,
                            },
                        )}
                        textPosition={textPosition}
                        width={width}
                        height={height}
                        {...omit(resolvedProps, ['title', 'description'])}
                        src={(resolvedProps.data || resolvedProps.url) as string}
                    />
                    {hasStatuses && <ImageStatuses statuses={statuses} model={model} className="image-field-statuses" />}
                </div>
            </ActionWrapper>
            <ImageInfo title={title} description={description} />
        </div>
    )
}

const mapStateToProps = (state: State, { modelPrefix, form }: Props) => {
    const model = (!modelPrefix || !form) ? {} : state.models[modelPrefix][form] as Record<string, unknown>

    return { model }
}

export const ImageField = connect(mapStateToProps)(ImageFieldBody)
export default ImageField
