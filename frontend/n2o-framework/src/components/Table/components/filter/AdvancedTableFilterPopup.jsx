import React, { useCallback } from 'react'
import { pure } from 'recompose'
import PropTypes from 'prop-types'
import { Button } from 'reactstrap'
import classNames from 'classnames'

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

const validationMap = {
    'is-valid': 'text-success',
    'is-invalid': 'text-danger',
    'has-warning': 'text-warning',
}

function AdvancedTableFilterPopup({
    value,
    onChange,
    onSearchClick,
    onResetClick,
    component,
    componentProps,
    error,
    style,
}) {
    const onKeyDown = useCallback((event) => {
        if (event.key === 'Enter') {
            onSearchClick()
        }
    }, [onSearchClick])

    return (
        <>
            <div className="n2o-advanced-table-filter-dropdown-popup" style={style}>
                {component ? (
                    React.createElement(component, {
                        ...componentProps,
                        value,
                        onChange,
                        onKeyDown,
                        popupPlacement: 'right',
                        touched: true,
                        ...error,
                    })
                ) : (
                    <div className="n2o-form-group">
                        <InputText
                            className={error.validationClass}
                            value={value}
                            onChange={onChange}
                            onKeyDown={onKeyDown}
                        />
                        {error?.message?.text ? (
                            <div className={classNames(
                                'n2o-validation-message',
                                validationMap[error.validationClass],
                            )}
                            >
                                {error?.message?.text}
                            </div>
                        ) : null}
                    </div>
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
    error: PropTypes.object,
}

AdvancedTableFilterPopup.defaultProps = {
    onChange: () => {},
    onSearchClick: () => {},
    onResetClick: () => {},
    componentProps: {},
}

export { AdvancedTableFilterPopup }
export default pure(AdvancedTableFilterPopup)
