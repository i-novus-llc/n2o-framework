import React, { useCallback, memo, KeyboardEvent } from 'react'
import { Button } from 'reactstrap'
import classNames from 'classnames'
import { InputText } from '@i-novus/n2o-components/lib/inputs/InputText'

import { EMPTY_OBJECT, NOOP_FUNCTION } from '../../../../utils/emptyTypes'

import { type AdvancedTableFilterPopupProps, VALIDATION_MAP } from './types'

/**
 * Компонент overlay для фильтра
 */

const AdvancedTableFilterPopupBody = ({
    value,
    component,
    style,
    touched = false,
    onChange = NOOP_FUNCTION,
    onBlur = NOOP_FUNCTION,
    onSearchClick = NOOP_FUNCTION,
    onResetClick = NOOP_FUNCTION,
    componentProps = EMPTY_OBJECT,
    error = EMPTY_OBJECT,
}: AdvancedTableFilterPopupProps) => {
    const onKeyDown = useCallback((event: KeyboardEvent) => {
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
                        onBlur,
                        onKeyDown,
                        popupPlacement: 'right',
                        touched,
                        strategy: 'absolute',
                        label: null,
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
                        {error?.message?.text && (
                            <div className={classNames(
                                'n2o-validation-message',
                                VALIDATION_MAP[error.validationClass || ''],
                            )}
                            >
                                {error.message.text}
                            </div>
                        )}
                    </div>
                )}
            </div>
            <div className="n2o-advanced-table-filter-dropdown-buttons">
                <Button color="primary" size="sm" onClick={onSearchClick}>Найти</Button>
                <Button size="sm" onClick={onResetClick}>Сбросить</Button>
            </div>
        </>
    )
}

export const AdvancedTableFilterPopup = memo(AdvancedTableFilterPopupBody)
export default AdvancedTableFilterPopup
