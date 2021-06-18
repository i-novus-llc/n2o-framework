import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import set from 'lodash/set'
import isEqual from 'lodash/isEqual'
import {
    compose,
    withState,
    lifecycle,
    withHandlers,
    setDisplayName,
} from 'recompose'

import withCell from '../../withCell'
import withTooltip from '../../withTooltip'
// eslint-disable-next-line import/no-named-as-default
import CheckboxN2O from '../../../../controls/Checkbox/CheckboxN2O'

function CheckboxCell({
    model,
    fieldKey,
    id,
    visible,
    disabled,
    checked,
    handleClick,
    handleChange,
    ...rest
}) {
    return (
        visible && (
            <CheckboxN2O
                className="сheckbox-сell"
                inline
                onClick={handleClick}
                onChange={handleChange}
                disabled={disabled}
                checked={checked}
                {...rest}
            />
        )
    )
}

CheckboxCell.propTypes = {
    /**
   * ID чейки
   */
    id: PropTypes.string,
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
   * Флаг видимости
   */
    visible: PropTypes.bool,
}

CheckboxCell.defaultProps = {
    visible: true,
    disabled: false,
}

export { CheckboxCell }
export default compose(
    setDisplayName('CheckboxCell'),
    withCell,
    withTooltip,
    withState(
        'checked',
        'setChecked',
        ({ model, fieldKey, id }) => model && get(model, fieldKey || id),
    ),
    withHandlers({
        handleClick: () => (e) => {
            e.stopPropagation()
        },
        handleChange: ({ callAction, setChecked, model, fieldKey, id }) => (e) => {
            const { checked } = e.nativeEvent.target

            const data = set(
                {
                    ...model,
                },
                fieldKey || id,
                checked,
            )

            setChecked(checked)
            callAction(data)
        },
    }),
    lifecycle({
        componentDidUpdate(prevProps) {
            const { model, fieldKey, id } = this.props

            if (
                !isEqual(
                    get(prevProps.model, fieldKey || id),
                    get(model, fieldKey || id),
                )
            ) {
                this.setState({ checked: get(model, fieldKey || id) })
            }
        },
    }),
)(CheckboxCell)
