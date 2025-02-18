import React, { CSSProperties, useContext } from 'react'
import classNames from 'classnames'
import omit from 'lodash/omit'
import get from 'lodash/get'
import has from 'lodash/has'
import { EventHandlersContext } from '@i-novus/n2o-components/lib/inputs/eventHandlersContext'
import { Spinner, SpinnerType } from '@i-novus/n2o-components/lib/layouts/Spinner/Spinner'

import { Toolbar, type ToolbarProps } from '../../../../buttons/Toolbar'
import { useResolved } from '../../../../../core/Expression/useResolver'

import { Control, type Props as ControlProps } from './Control'
import { Label } from './Label'
import { Measure } from './Measure'
import { Description } from './Description'
import { FieldActions, type Props as FieldActionsProps } from './FieldActions'

export enum VALIDATION_CLASSES {
    'is-valid' = 'text-success',
    'is-invalid' = 'text-danger',
    'has-warning' = 'text-warning',
}

export const getValidationClass = (validationClass?: VALIDATION_CLASSES) => get(VALIDATION_CLASSES, validationClass || '')

export interface Props extends ControlProps {
    id: string
    label?: string
    control?: Pick<ControlProps, 'component' | 'className' | 'value'>
    description?: string
    measure?: string
    required?: boolean
    className?: string
    labelPosition?: 'top-left' | 'top-right' | 'left' | 'right'
    labelAlignment?: 'left' | 'right'
    labelWidth?: string | number
    style?: CSSProperties
    fieldActions?: FieldActionsProps['actions']
    loading?: boolean
    labelStyle?: CSSProperties
    labelClass?: string
    validationClass?: VALIDATION_CLASSES
    touched?: boolean
    message?: { text: string }
    colLength?: number
    toolbar?: ToolbarProps
    form?: string
    noLabelBlock?: boolean | string
    noLabel?: boolean | string
    containerKey?: string
    dataProvider?: object
}

export const StandardField = ({
    id,
    value,
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
    touched,
    message,
    colLength,
    help,
    toolbar,
    form,
    noLabel: propsNoLabel,
    noLabelBlock: propsNoLabelBlock,
    onChange = () => {},
    loading = false,
    required = false,
    className = '',
    control = { component: '' },
    visible = true,
    style = {},
    enabled = true,
    disabled = false,
    ...rest
}: Props) => {
    const eventHandlerContext = useContext(EventHandlersContext)

    const {
        noLabelBlock = false,
        noLabel = false,
    } = useResolved({ noLabelBlock: propsNoLabelBlock, noLabel: propsNoLabel }, rest.model)

    if (!visible) { return null }

    const getLabelWidthPixels = (labelWidth?: string | number) => {
        switch (labelWidth) {
            case 'default': return 180
            case 'min':
            case '100%': return undefined
            default: return labelWidth
        }
    }

    const labelWidthPixels = getLabelWidthPixels(labelWidth)

    const styleHelper = labelWidthPixels && colLength && colLength > 1
        ? { maxWidth: `calc(100% - ${labelWidthPixels})` } : { width: '100%' }

    const extendedLabelStyle = {
        width: labelWidthPixels,
        flex: labelWidthPixels ? 'none' : undefined,
        ...labelStyle,
    }

    const fieldId = `field-${form}-id`
    const controlValidationClass = (validationClass && touched) ? validationClass : ''

    return (
        <div
            id={fieldId}
            className={classNames('n2o-form-group', 'form-group', className, {
                [`label-${labelPosition}`]: labelPosition,
                'n2o-form-group--disabled': loading,
            })}
            style={style}
        >
            {!noLabelBlock && (
                <Label
                    id={id}
                    value={noLabel ? null : label}
                    style={extendedLabelStyle}
                    className={classNames(
                        labelClass,
                        { [`label-alignment-${labelAlignment}`]: labelAlignment },
                        'n2o-label',
                    )}
                    required={required}
                    help={help}
                    needStub
                />
            )}
            <div style={styleHelper}>
                <div className={classNames('form-container', { 'form-container_with-toolbar': toolbar })}>
                    <Control
                        placeholder={placeholder}
                        visible={visible}
                        autoFocus={autoFocus}
                        {...eventHandlerContext}
                        onBlur={onBlur}
                        onFocus={onFocus}
                        onChange={onChange}
                        help={help}
                        enabled={enabled}
                        disabled={disabled}
                        {...omit(rest, ['dataProvider', 'containerKey', 'controlClass', 'controlStyle'])}
                        {...control}
                        value={has(rest.model, id) ? value : get(control, 'value', null)}
                        className={classNames(control.className, controlValidationClass, { 'form-control__with-toolbar': toolbar })}
                        model={rest.model}
                    />
                    {toolbar && (
                        <Toolbar
                            className="n2o-page-actions__form-toolbar"
                            toolbar={toolbar}
                            entityKey={form}
                        />
                    )}
                    <Measure value={measure} />
                    <FieldActions actions={fieldActions} />
                    {loading && (
                        <Spinner
                            className="n2o-form-group__spinner align-self-center"
                            type={SpinnerType.inline}
                            size="sm"
                        />
                    )}
                </div>
                <Description value={description} />
                <div className={classNames('n2o-validation-message', getValidationClass(validationClass))}>
                    {touched && message && message.text}
                </div>
            </div>
        </div>
    )
}

export default StandardField
