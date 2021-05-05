/**
 * Created by emamoshin on 27.09.2017.
 */
import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'

import Text from '../../../../snippets/Typography/Text/Text'
import withTooltip from '../../withTooltip'

import SubText from './SubText'

/** Описание */
function TextCell({
    model,
    fieldKey,
    id,
    visible,
    preLine,
    subTextFieldKey,
    subTextFormat,
    ...rest
}) {
    return (
        visible && (
            <div className="d-inline-flex flex-column">
                <Text
                    text={model && get(model, fieldKey || id)}
                    preLine={preLine}
                    {...rest}
                />
                {subTextFieldKey ? (
                    <SubText
                        subText={model && get(model, subTextFieldKey)}
                        format={subTextFormat}
                    />
                ) : null}
            </div>
        )
    )
}

TextCell.propTypes = {
    /**
   * Модель данных
   */
    model: PropTypes.object,
    /**
   * Ключ значения из модели
   */
    fieldKey: PropTypes.string,
    /**
   * Класс
   */
    className: PropTypes.string,
    /**
   * Формат
   */
    format: PropTypes.string,
    /**
   * Ключ значения сабтекста из модели
   */
    subTextFieldKey: PropTypes.string,
    /**
   * Формат сабтекста
   */
    subTextFormat: PropTypes.string,
    /**
   * Флаг видимости
   */
    visible: PropTypes.bool,
}

TextCell.defaultProps = {
    visible: true,
}

export { TextCell }

export default withTooltip(TextCell)
