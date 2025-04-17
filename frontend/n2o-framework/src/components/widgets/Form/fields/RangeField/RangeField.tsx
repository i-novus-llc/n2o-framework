import React, { useCallback, useMemo } from 'react'
import classNames from 'classnames'
import { InlineSpinner } from '@i-novus/n2o-components/lib/layouts/Spinner/InlineSpinner'

import { Control, type ControlCommonProps, type RangeValue } from '../StandardField/Control'
import { Measure } from '../StandardField/Measure'
import { Label } from '../StandardField/Label'
import { FieldActions } from '../StandardField/FieldActions'
import { Description } from '../StandardField/Description'
import { type Props as StandardFieldProps, getValidationClass } from '../StandardField/StandardField'
import { EMPTY_OBJECT, NOOP_FUNCTION } from '../../../../../utils/emptyTypes'

export interface Props extends Omit<StandardFieldProps, 'component' | 'value'> {
    beginControl: ControlCommonProps
    endControl: ControlCommonProps
    divider?: string | boolean
    value: RangeValue
}

export const RangeField = ({
    value,
    beginControl,
    endControl,
    id,
    label,
    description,
    measure,
    labelPosition,
    labelAlignment,
    labelWidth,
    fieldActions,
    autoFocus,
    labelStyle,
    labelClass,
    validationClass,
    onFocus,
    onBlur,
    placeholder,
    message,
    help,
    visible = true,
    required = false,
    className = '',
    loading = false,
    style = EMPTY_OBJECT,
    enabled = true,
    disabled = false,
    onChange = NOOP_FUNCTION,
    divider = false,
    ...rest
}: Props) => {
    const { begin } = value || {}
    const { end } = value || {}

    const onBeginValueChange = useCallback((begin) => {
        if (begin !== '') { onChange({ begin, end }) }
    }, [end, onChange])

    const onEndValueChange = useCallback((end) => {
        if (end !== '') { onChange({ begin, end }) }
    }, [begin, onChange])

    const labelWidthPixels = useMemo(() => {
        switch (labelWidth) {
            case 'default': return 180
            case 'min':
            case '100%': return undefined
            default: return labelWidth
        }
    }, [labelWidth])

    if (!visible) { return null }

    const extendedLabelStyle = { width: labelWidthPixels, flex: labelWidthPixels ? 'none' : undefined, ...labelStyle }

    return (
        <div
            className={classNames(
                'n2o-range-field',
                'n2o-form-group',
                'form-group',
                className,
                {
                    [`label-${labelPosition}`]: labelPosition,
                },
            )}
            style={style}
        >
            <Label
                id={id}
                value={label}
                style={extendedLabelStyle}
                className={classNames(labelClass, 'n2o-label', { [`label-alignment-${labelAlignment}`]: labelAlignment })}
                required={required}
                help={help}
            />
            <div className="n2o-range-field-body">
                <div
                    className={classNames(
                        'n2o-range-field-controls-container',
                        'd-flex',
                        'align-items-center',
                        {
                            'n2o-range-field-body--divider': !divider,
                        },
                    )}
                >
                    <div className="n2o-range-field-start n2o-range-field-item mr-3">
                        <div className="d-flex align-items-center">
                            <Control
                                value={begin}
                                placeholder={placeholder}
                                visible={visible}
                                autoFocus={autoFocus}
                                onBlur={(value) => {
                                    if (typeof value === 'string' || typeof value === 'number') {
                                        onBlur?.({ begin: value, end })
                                    }
                                }}
                                onFocus={onFocus}
                                onChange={onBeginValueChange}
                                {...beginControl}
                                {...rest}
                                enabled={enabled}
                                disabled={disabled}
                                component={beginControl.component}
                                className={classNames(beginControl?.className, validationClass)}
                            />
                            <Measure value={measure} />
                        </div>
                    </div>
                    {divider && <div className="n2o-range-field-divider">{divider}</div>}
                    <div className="n2o-range-field-end n2o-range-field-item ml-3">
                        <div className="d-flex align-items-center">
                            <Control
                                value={end}
                                placeholder={placeholder}
                                visible={visible}
                                autoFocus={false}
                                onBlur={(value) => {
                                    if (typeof value === 'string' || typeof value === 'number') {
                                        onBlur?.({ begin, end: value })
                                    }
                                }}
                                onFocus={onFocus}
                                onChange={onEndValueChange}
                                {...endControl}
                                {...rest}
                                enabled={enabled}
                                disabled={disabled}
                                component={endControl.component}
                                className={classNames(endControl?.className, validationClass)}
                            />
                            <Measure value={measure} />
                        </div>
                    </div>
                </div>
                {loading && <InlineSpinner />}
                <FieldActions actions={fieldActions} />
            </div>
            <Description value={description} />
            <div className={classNames('n2o-validation-message', getValidationClass(validationClass))}>
                {message?.text}
            </div>
        </div>
    )
}

export default RangeField
