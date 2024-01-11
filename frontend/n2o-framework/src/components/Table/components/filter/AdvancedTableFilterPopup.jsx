import React from 'react'
import { pure } from 'recompose'
import PropTypes from 'prop-types'
import { Button } from 'reactstrap'

import InputText from '../../../controls/InputText/InputText'

/**
 * Компонент overlay для фильтра
 * @param value - значение фильтра
 * @param onChange - callback на изменение
 * @param onSearchClick - callback на поиск
 * @param onResetClick - callback на сброс
 * @param component - компонент контрол фильтра
 * @param controlProps
 * @returns {*}
 * @constructor
 */

function AdvancedTableFilterPopup({
    value,
    onChange,
    onSearchClick,
    onResetClick,
    component,
    componentProps,
}) {
    const onKeyDown = (event) => {
        if (event.key === 'Enter') { onSearchClick() }
    }

    return (
        <>
            <div className="n2o-advanced-table-filter-dropdown-popup">
                {component ? (
                    React.createElement(component, { ...componentProps, value, onChange, onKeyDown, popupPlacement: 'right' })
                ) : (
                    <InputText value={value} onChange={onChange} onKeyDown={onKeyDown} />
                )}
            </div>
            <div className="n2o-advanced-table-filter-dropdown-buttons">
                <Button color="primary" size="sm" onClick={onSearchClick}>Искать</Button>
                <Button size="sm" onClick={onResetClick}>Сбросить</Button>
            </div>
        </>
    )
}

AdvancedTableFilterPopup.propTypes = {
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    onChange: PropTypes.func,
    component: PropTypes.any,
    onSearchClick: PropTypes.func,
    onResetClick: PropTypes.func,
    componentProps: PropTypes.object,
}

AdvancedTableFilterPopup.defaultProps = {
    onChange: () => {},
    onSearchClick: () => {},
    onResetClick: () => {},
    componentProps: {},
}

export { AdvancedTableFilterPopup }
export default pure(AdvancedTableFilterPopup)
