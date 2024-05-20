import React from 'react'
import PropTypes from 'prop-types'
import get from 'lodash/get'
import set from 'lodash/set'
import isEqual from 'lodash/isEqual'
import omit from 'lodash/omit'
import {
    compose,
    withState,
    lifecycle,
    withHandlers,
    setDisplayName,
} from 'recompose'
import { push } from 'connected-react-router'

import withCell from '../../withCell'
import withTooltip from '../../withTooltip'
import { Checkbox } from '../../../../controls/Checkbox/Checkbox'

function CheckboxCell({
    checked,
    handleClick,
    handleChange,
    visible = true,
    disabled = false,
    ...rest
}) {
    if (!visible) { return null }

    return (
        <Checkbox
            className="сheckbox-сell"
            inline
            onClick={handleClick}
            onChange={handleChange}
            disabled={disabled}
            checked={checked}
            {...omit(rest, ['id', 'fieldKey', 'model'])}
        />
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
        handleClick: () => (e) => { e.stopPropagation() },
        handleChange: ({
            callAction,
            setChecked,
            model,
            fieldKey,
            id,
            url,
            target,
            dispatch,
        }) => (e) => {
            const { checked } = e.nativeEvent.target

            // TODO сделать единый redirect func для кнопок, ячеек, linkImpl, meta saga итд.
            //  разобраться с синтаксисом, прим. где-то _self, а где-то self
            const direct = () => {
                if (!url) { return null }

                if (target === 'application') {
                    dispatch(push(url))

                    return null
                }

                if (target === '_blank') {
                    window.open(url)

                    return null
                }

                window.location = url

                return null
            }

            const data = set({ ...model }, fieldKey || id, checked)

            setChecked(checked)
            callAction(data)
            direct()
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
