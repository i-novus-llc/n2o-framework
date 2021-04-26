import React from 'react'
import { connect } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'

import propsResolver from '../../../../../utils/propsResolver'
import Image from '../../../../snippets/Image/Image'

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
 */

function ImageField(props) {
    const {
        id,
        url,
        data,
        title,
        description,
        textPosition,
        width,
        shape,
        visible,
        model,
    } = props

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
            textPosition={textPosition}
            width={width}
            shape={shape}
            visible={visible}
            {...resolveProps}
            src={resolveProps.data || resolveProps.url}
        />
    )
}

const mapStateToProps = (state, { modelPrefix, form }) => {
    const model =
    isNil(modelPrefix) || isNil(form) ? {} : state.models[modelPrefix][form]
    return {
        model,
    }
}

export default connect(
    mapStateToProps,
    null,
)(ImageField)
