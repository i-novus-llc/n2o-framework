import React from 'react'
import { pure } from 'recompose'
import PropTypes from 'prop-types'
import { Button } from 'reactstrap'
import assign from 'lodash/assign'

import InputText from '../../controls/InputText/InputText'

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
    controlProps,
}) {
    const childProps = {
        ...controlProps,
        value,
        onChange,
    }

    /* eslint-disable react/jsx-one-expression-per-line */
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
                <Button color="primary" size="sm" onClick={onSearchClick}>

          Искать
                </Button>
                <Button size="sm" onClick={onResetClick}>

          Сбросить
                </Button>
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
    controlProps: PropTypes.object,
}

AdvancedTableFilterPopup.defaultProps = {
    onChange: () => {},
    onSearchClick: () => {},
    onResetClick: () => {},
    controlProps: {},
}

export { AdvancedTableFilterPopup }
export default pure(AdvancedTableFilterPopup)
