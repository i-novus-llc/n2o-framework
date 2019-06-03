import React from 'react';
import PropTypes from 'prop-types';
import { compose, withHandlers, mapProps } from 'recompose';
import { get } from 'lodash';
import Control from '../StandardField/Control';
import Measure from '../StandardField/Measure';
import Label from '../StandardField/Label';
import FieldActions from '../StandardField/FieldActions';
import InlineSpinner from '../../../../snippets/Spinner/InlineSpinner';
import Description from '../StandardField/Description';
import cx from 'classnames';
import { FieldActionsPropTypes } from '../StandardField/FieldPropTypes';

/**
 * @return {null}
 */
function RangeField({
  beginControl,
  endControl,
  id,
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
  onFocus,
  onBlur,
  placeholder,
  touched,
  message,
  colLength,
  help,
  value,
  onChange,
  onBeginValueChange,
  onEndValueChange,
  begin,
  end,
  divider,
  ...props
}) {
  const flexStyle = { display: 'flex' };
  const validationMap = {
    'is-valid': 'text-success',
    'is-invalid': 'text-danger',
    'has-warning': 'text-warning',
  };
  const labelWidthPixels =
    labelWidth === 'default'
      ? 180
      : labelWidth === 'min' || labelWidth === '100%'
      ? undefined
      : labelWidth;

  const extendedLabelStyle = {
    width: labelWidthPixels,
    flex: labelWidthPixels ? 'none' : undefined,
    ...labelStyle,
  };

  return visible ? (
    <div
      className={cx(
        'n2o-range-field',
        'n2o-form-group',
        'form-group',
        className,
        {
          ['label-' + labelPosition]: labelPosition,
        }
      )}
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
      <div
        className={cx('n2o-range-field-body', {
          'n2o-range-field-body--divider': !divider,
        })}
      >
        <div className="n2o-range-field-start n2o-range-field-item mr-3">
          <div style={flexStyle}>
            <Control
              placeholder={placeholder}
              visible={visible}
              autoFocus={autoFocus}
              value={begin}
              onBlur={onBlur}
              onFocus={onFocus}
              onChange={onBeginValueChange}
              {...beginControl}
              {...props}
              className={cx(beginControl && beginControl.className, {
                [validationClass]: touched,
              })}
            />
            <Measure value={measure} />
            <FieldActions actions={fieldActions} />
            {loading && <InlineSpinner />}
          </div>
        </div>
        {divider && <div className="n2o-range-field-divider">{divider}</div>}
        <div className="n2o-range-field-end n2o-range-field-item ml-3">
          <div style={flexStyle}>
            <Control
              placeholder={placeholder}
              visible={visible}
              autoFocus={false}
              value={end}
              onBlur={onBlur}
              onFocus={onFocus}
              onChange={onEndValueChange}
              {...endControl}
              {...props}
              className={cx(endControl && endControl.className, {
                [validationClass]: touched,
              })}
            />
            <Measure value={measure} />
            <FieldActions actions={fieldActions} />
            {loading && <InlineSpinner />}
          </div>
        </div>
      </div>
      <Description value={description} />
      <div
        className={cx('n2o-validation-message', validationMap[validationClass])}
      >
        {touched && message && message.text}
      </div>
    </div>
  ) : null;
}

RangeField.propTypes = {
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
};

RangeField.defaultProps = {
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
};

export default compose(
  mapProps(props => ({
    ...props,
    begin: get(props, 'value.begin', null),
    end: get(props, 'value.end', null),
  })),
  withHandlers({
    onBeginValueChange: ({ end, onChange }) => begin =>
      onChange({ begin, end }),
    onEndValueChange: ({ begin, onChange }) => end => onChange({ begin, end }),
  })
)(RangeField);
