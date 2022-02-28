import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import Toolbar from '../../../../buttons/Toolbar'
import { Spinner } from '../../../../snippets/Spinner/Spinner'

import Control from './Control'
import Label from './Label'
import Measure from './Measure'
import Description from './Description'
import FieldActions from './FieldActions'
import { FieldActionsPropTypes } from './FieldPropTypes'

/**
 * Компонент - поле формы
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
 * @reactProps {string|element} control - строка с названием компонента (тем, которое указано в мэпе index.js) или элемент
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
 * @example
 * <Field onChange={this.onChange}
 *             id='DistanceInput'
 *             control='Input'
 *             label="Расстояние"
 *             measure="км"
 *             description="Введите расстояние от пункта А до пункта Б"
 *             style={display: 'inline-block'}/>
 */
class StandardField extends React.Component {
    render() {
        const {
            id,
            value,
            visible,
            label,
            control,
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
            controlStyle,
            labelClass,
            validationClass,
            controlClass,
            onChange,
            onFocus,
            onBlur,
            placeholder,
            touched,
            message,
            colLength,
            help,
            toolbar,
            containerKey,
            dataProvider,
            form,
            noLabelBlock,
            ...props
        } = this.props

        const validationMap = {
            'is-valid': 'text-success',
            'is-invalid': 'text-danger',
            'has-warning': 'text-warning',
        }

        const getLabelWidthPixels = (labelWidth) => {
            switch (labelWidth) {
                case 'default':
                    return 180
                case 'min' || '100%':
                    return undefined
                default:
                    return labelWidth
            }
        }

        const labelWidthPixels = getLabelWidthPixels(labelWidth)

        const styleHelper = labelWidthPixels && colLength > 1
            ? {
                maxWidth: `calc(100% - ${labelWidthPixels})`,
            }
            : { width: '100%' }

        const extendedLabelStyle = {
            width: labelWidthPixels,
            flex: labelWidthPixels ? 'none' : undefined,
            ...labelStyle,
        }

        const fieldId = `field-${props.form}-id`

        return (
            visible && (
                <div
                    id={fieldId}
                    className={classNames('n2o-form-group', 'form-group', className, {
                        [`label-${labelPosition}`]: labelPosition,
                        'n2o-form-group--disabled': loading,
                    })}
                    style={style}
                >
                    {!noLabelBlock ? (
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
                    ) : null}
                    <div style={styleHelper}>
                        <div
                            className={classNames('form-container', {
                                'form-container_with-toolbar': toolbar,
                            })}
                        >
                            <Control
                                placeholder={placeholder}
                                visible={visible}
                                autoFocus={autoFocus}
                                value={value}
                                onBlur={onBlur}
                                onFocus={onFocus}
                                onChange={onChange}
                                help={help}
                                {...props}
                                {...control}
                                className={classNames(control.className, {
                                    [validationClass]: validationClass && touched,
                                    'form-control__with-toolbar': toolbar,
                                })}
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
                                    type="inline"
                                    size="sm"
                                />
                            )}
                        </div>
                        <Description value={description} />
                        <div
                            className={classNames(
                                'n2o-validation-message',
                                validationMap[validationClass],
                            )}
                        >
                            {touched && message && message.text}
                        </div>
                    </div>
                </div>
            )
        )
    }
}

StandardField.propTypes = {
    label: PropTypes.oneOfType([PropTypes.string, PropTypes.element]),
    control: PropTypes.oneOfType([
        PropTypes.func,
        PropTypes.node,
        PropTypes.string,
        PropTypes.object,
    ]),
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
    id: PropTypes.string,
    labelStyle: PropTypes.object,
    controlStyle: PropTypes.object,
    labelClass: PropTypes.string,
    validationClass: PropTypes.oneOfType([PropTypes.string, PropTypes.oneOf([false])]),
    controlClass: PropTypes.string,
    enabled: PropTypes.bool,
    onFocus: PropTypes.func,
    onBlur: PropTypes.func,
    placeholder: PropTypes.string,
    toolbar: PropTypes.array,
    colLength: PropTypes.number,
    containerKey: PropTypes.string,
    dataProvider: PropTypes.object,
    form: PropTypes.string,
    noLabelBlock: PropTypes.bool,
    value: PropTypes.any,
}

StandardField.defaultProps = {
    visible: true,
    required: false,
    control: <input />,
    loading: false,
    className: '',
    style: {},
    enabled: true,
    disabled: false,
    onChange: () => {},
}

export default StandardField
