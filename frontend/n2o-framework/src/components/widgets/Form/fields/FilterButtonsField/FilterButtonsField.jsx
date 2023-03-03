import React from 'react'
import PropTypes from 'prop-types'

import Buttons from '../../../../snippets/Filter/Buttons'
import { useWidgetFilterContext } from '../../../WidgetFilters'

/**
 * Компонент обертка для встраивания кнопок фильтра в любое место формы, как Field.
 * Методы берутся из контекста, поэтому обязательно использовать внутри WidgetFilters
 * @reactProps {boolean} visible
 * @reactProps {string} searchLabel
 * @reactProps {string} resetLabel
 */
const FilterButtonsField = (props) => {
    const { visible = true, searchLabel, resetLabel, className, disabled = false } = props
    const { filter, reset } = useWidgetFilterContext()

    if (!visible) {
        return null
    }

    return (
        visible ? (
            <Buttons
                className={className}
                disabled={disabled}
                searchLabel={searchLabel}
                resetLabel={resetLabel}
                onSearch={filter}
                onReset={reset}
            />
        ) : null
    )
}

FilterButtonsField.propTypes = {
    className: PropTypes.string,
    searchLabel: PropTypes.string,
    resetLabel: PropTypes.string,
    visible: PropTypes.bool,
    disabled: PropTypes.bool,
}

export default FilterButtonsField
