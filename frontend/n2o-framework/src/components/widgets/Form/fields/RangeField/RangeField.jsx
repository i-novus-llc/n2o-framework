import React, { useMemo } from 'react'
import PropTypes from 'prop-types'
import { compose, withHandlers, mapProps } from 'recompose'
import get from 'lodash/get'
import classNames from 'classnames'
import { omit } from 'lodash'

import Control from '../StandardField/Control'
import Measure from '../StandardField/Measure'
import Label from '../StandardField/Label'
import FieldActions from '../StandardField/FieldActions'
import InlineSpinner from '../../../../snippets/Spinner/InlineSpinner'
import Description from '../StandardField/Description'
import { FieldActionsPropTypes } from '../StandardField/FieldPropTypes'

/**
 * Компонент - RangeField формы
 * @reactProps {string} id - уникальный идентификатор поля
 * @reactProps {boolean} visible - отображать / не отображать Поле
 * @reactProps {string} label - лэйбл поля
 * @reactProps {string} labelClass - css-класс для лейбела
 * @reactProps {string} controlClass - css-класс для контрола
 * @reactProps {object} labelStyle- объект стилей для лейбела
 * @reactProps {object} controlStyle - объект стилей для контрола
 * @reactProps {string} className - css-класс для поля
 * @reactProps {boolean} required - обязательное / необязательное поле
 * @reactProps {boolean} disabled - контрол доступен только для чтения / нет
 * @reactProps {boolean} enabled - контрол активирован / нет
 * @reactProps {string|element} control - строка с названием компонента (тем, которое указано в мэпе index.tsx) или элемент
 * @reactProps {string} description - описание поля (находится под контролом)
 * @reactProps {string} measure - единица измерения, находится после контрола (например, км, кг, л)
 * @reactProps {object} style - объект с css-стилями для поля
 * @reactProps {object} fieldActions - объект для создания экшенов, связанных с полем
 * @reactProps {function} onChange - вызывается при изменении контрола
 * @reactProps {boolean} loading - показывать лоадер(спиннер) или нет
 * @reactProps {boolean} autofocus - есть автофокус на это поле или нет
 * @reactProps {string} validationClass - css-класс валидации(has-error, has-warning или has-success)
 * @reactProps {object} message  - содержит поле text c текстом сообщения(ошибки)
 * @reactProps {string|node} help - подскзка рядом с лейблом
 * @reactProps {string|boolean} - разделитель между полями
 * @return {node|null}
 * @example
 * <RangeField onChange={this.onChange}
 *             id='DistanceInput'
 *             control='Input'
 *             label="Расстояние"
 *             measure="км"
 *             description="Введите расстояние от пункта А до пункта Б"
 *             style={display: 'inline-block'}/>
 */
export function RangeFieldComponent({
    beginControl,
    endControl,
    id,
    visible,
    label,
    description,
    measure,
    required,
    className,
    labelPosition,
    labelAlignment,
    labelWidth,
    style,
    fieldActions,
    loading,
    autoFocus,
    labelStyle,
    labelClass,
    validationClass,
    onFocus,
    onBlur,
    placeholder,
    touched,
    message,
    help,
    onBeginValueChange,
    onEndValueChange,
    begin,
    end,
    divider,
    ...props
}) {
    const omited = omit(props, ['control', 'controlClass', 'value', 'onChange', 'controlStyle'])
    const validationMap = {
        'is-valid': 'text-success',
        'is-invalid': 'text-danger',
        'has-warning': 'text-warning',
    }

    const labelWidthPixels = useMemo(() => {
        switch (labelWidth) {
            case 'default':
                return 180
            case 'min' || '100%':
                return undefined
            default:
                return labelWidth
        }
    }, [labelWidth])

    const extendedLabelStyle = {
        width: labelWidthPixels,
        flex: labelWidthPixels ? 'none' : undefined,
        ...labelStyle,
    }

    return visible ? (
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
                className={classNames(
                    labelClass,
                    { [`label-alignment-${labelAlignment}`]: labelAlignment },
                    'n2o-label',
                )}
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
                            {/* FIXME: Не совсем понятно почему пришлось пробросить компонент явно. Если этого не сделать, пробрасывается что то не то*/}
                            <Control
                                placeholder={placeholder}
                                visible={visible}
                                autoFocus={autoFocus}
                                value={begin}
                                onBlur={value => onBlur({ begin: value, end })}
                                onFocus={onFocus}
                                onChange={onBeginValueChange}
                                {...beginControl}
                                {...omited}
                                component={beginControl.component}
                                className={classNames(beginControl && beginControl.className, {
                                    [validationClass]: touched,
                                })}
                            />
                            <Measure value={measure} />
                        </div>
                    </div>
                    {divider && <div className="n2o-range-field-divider">{divider}</div>}
                    <div className="n2o-range-field-end n2o-range-field-item ml-3">
                        <div className="d-flex align-items-center">
                            {/* FIXME: Не совсем понятно почему пришлось пробросить компонент явно. Если этого не сделать, пробрасывается что то не то*/}
                            <Control
                                placeholder={placeholder}
                                visible={visible}
                                autoFocus={false}
                                value={end}
                                onBlur={value => onBlur({ begin, end: value })}
                                onFocus={onFocus}
                                onChange={onEndValueChange}
                                {...endControl}
                                {...omited}
                                component={endControl.component}
                                className={classNames(endControl && endControl.className, {
                                    [validationClass]: touched,
                                })}
                            />
                            <Measure value={measure} />
                        </div>
                    </div>
                </div>
                {loading && <InlineSpinner />}
                <FieldActions actions={fieldActions} />
            </div>
            <Description value={description} />
            <div
                className={classNames('n2o-validation-message', validationMap[validationClass])}
            >
                {touched && message && message.text}
            </div>
        </div>
    ) : null
}

RangeFieldComponent.propTypes = {
    label: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
    control: PropTypes.node,
    visible: PropTypes.bool,
    required: PropTypes.bool,
    disabled: PropTypes.bool,
    autoFocus: PropTypes.bool,
    onChange: PropTypes.func,
    description: PropTypes.string,
    measure: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
    fieldActions: FieldActionsPropTypes,
    valiastionClass: PropTypes.string,
    loading: PropTypes.bool,
    touched: PropTypes.bool,
    labelWidth: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    labelAlignment: PropTypes.oneOf(['left', 'right']),
    labelPosition: PropTypes.oneOf(['top-left', 'top-right', 'left', 'right']),
    message: PropTypes.object,
    help: PropTypes.oneOfType([PropTypes.string, PropTypes.node]),
    divider: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
    beginControl: PropTypes.object,
    endControl: PropTypes.object,
    id: PropTypes.string,
    labelStyle: PropTypes.object,
    controlStyle: PropTypes.object,
    labelClass: PropTypes.string,
    validationClass: PropTypes.string,
    controlClass: PropTypes.string,
    onFocus: PropTypes.func,
    onBlur: PropTypes.func,
    placeholder: PropTypes.string,
    value: PropTypes.object,
    onBeginValueChange: PropTypes.func,
    onEndValueChange: PropTypes.func,
    begin: PropTypes.oneOf(['string', null]),
    end: PropTypes.oneOf(['string', null]),
    enabled: PropTypes.bool,
}

RangeFieldComponent.defaultProps = {
    visible: true,
    required: false,
    control: <input />,
    loading: false,
    className: '',
    style: {},
    enabled: true,
    disabled: false,
    onChange: () => {},
    divider: false,
}

const isValid = period => period !== ''

export const RangeField = compose(
    mapProps(props => ({
        ...props,
        begin: get(props, 'value.begin', null),
        end: get(props, 'value.end', null),
    })),
    withHandlers({
        onBeginValueChange: ({ end, onChange }) => (begin) => {
            if (isValid(begin)) {
                onChange({ begin, end })
            }
        },

        onEndValueChange: ({ begin, onChange }) => (end) => {
            if (isValid(end)) {
                onChange({ begin, end })
            }
        },
    }),
)(RangeFieldComponent)

export default RangeField
