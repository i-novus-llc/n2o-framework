import React from 'react';
import PropTypes from 'prop-types';
import cx from 'classnames';

import Control from './Control';
import Label from './Label';

import Measure from './Measure';
import Description from './Description';
import FieldActions from './FieldActions';
import InlineSpinner from '../../../../snippets/Spinner/InlineSpinner';

import { FieldActionsPropTypes } from './FieldPropTypes';

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
  /**
   * Базовый рендер компонента
   */
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
      help,
      ...props
    } = this.props;
    const flexStyle = { display: 'flex' };
    const validationMap = {
      'is-valid': 'text-success',
      'is-invalid': 'text-danger',
      'has-warning': 'text-warning'
    };
    const styleHelper = { width: '100%' };
    const labelWidthPixels =
      labelWidth === 'default' ? 180 : labelWidth === 'min' ? undefined : labelWidth;
    const extendedLabelStyle = {
      width: labelWidthPixels,
      flex: labelWidthPixels ? 'none' : undefined,
      ...labelStyle
    };
    return (
      visible && (
        <div
          className={cx('n2o-form-group', 'form-group', className, {
            ['label-' + labelPosition]: labelPosition
          })}
          style={style}
        >
          <Label
            id={id}
            value={label}
            style={extendedLabelStyle}
            className={cx(
              labelClass,
              { ['label-alignment-' + labelAlignment]: labelAlignment },
              'n2o-label'
            )}
            required={required}
            help={help}
          />
          <div style={styleHelper}>
            <div style={flexStyle}>
              <Control
                className={cx(controlClass, { [validationClass]: touched })}
                style={controlStyle}
                placeholder={placeholder}
                visible={visible}
                autoFocus={autoFocus}
                value={value}
                onBlur={onBlur}
                onFocus={onFocus}
                onChange={onChange}
                {...control}
                {...props}
              />
              <Measure value={measure} />
              <FieldActions actions={fieldActions} />
              {loading && <InlineSpinner />}
            </div>
            <Description value={description} />
            <div className={cx('n2o-validation-message', validationMap[validationClass])}>
              {message && message.text}
            </div>
          </div>
        </div>
      )
    );
  }
}

StandardField.propTypes = {
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
  help: PropTypes.oneOf(PropTypes.string, PropTypes.node)
};

StandardField.defaultProps = {
  visible: true,
  required: false,
  control: <input />,
  loading: false,
  className: '',
  style: {},
  enabled: true,
  disabled: false,
  onChange: () => {}
};

export default StandardField;
