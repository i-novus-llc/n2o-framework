import React from 'react'
import { pure } from 'recompose'
import PropTypes from 'prop-types'
import Button from 'reactstrap/lib/Button'
import assign from 'lodash/assign'

import InputText from '../../controls/InputText/InputText'

/**
 * Компонент overlay для фильтра
 * @param value - значение фильтра
 * @param onChange - callback на изменение
 * @param onResetFilter - callback на сброс фильтра
 * @param onSetFilter - callback на поиск
 * @param component - компонент контрол фильтра
 * @param controlProps
 * @returns {*}
 * @constructor
 */
function AdvancedTableFilterPopup({
    value,
    onChange,
    onResetFilter,
    onSetFilter,
    component,
    controlProps,
}) {
    const childProps = {
        ...controlProps,
        value,
        onChange,
    }

    return (
        <>
            <div className="n2o-advanced-table-filter-dropdown-popup">
                {component ? (
                    React.createElement(
                        component,
                        assign({}, childProps, {
                            popupPlacement: 'right',
                        }),
                    )
                ) : (
                    <InputText value={value} onChange={onChange} />
                )}
            </div>
            <div className="n2o-advanced-table-filter-dropdown-buttons">
                <Button color="primary" size="sm" onClick={onSetFilter}>

          Искать
                </Button>
                <Button size="sm" onClick={onResetFilter}>

          Сбросить
                </Button>
            </div>
        </>
    )
}

AdvancedTableFilterPopup.propTypes = {
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    onChange: PropTypes.func,
    onResetFilter: PropTypes.func,
    onSetFilter: PropTypes.func,
    controlProps: PropTypes.object,
}

AdvancedTableFilterPopup.defaultProps = {
    onChange: () => {},
    onResetFilter: () => {},
    onSetFilter: () => {},
    controlProps: {},
}

export { AdvancedTableFilterPopup }
export default pure(AdvancedTableFilterPopup)
