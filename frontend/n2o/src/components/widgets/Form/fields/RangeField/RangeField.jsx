import React from 'react';
import PropTypes from 'prop-types';
import Control from '../StandardField/Control';
import Measure from '../StandardField/Measure';
import Label from '../StandardField/Label';
import FieldActions from '../StandardField/FieldActions';
import InlineSpinner from '../../../../snippets/Spinner/InlineSpinner';
import cx from 'classnames';
import { FieldActionsPropTypes } from '../StandardField/FieldPropTypes';

/**
 * @return {null}
 */
function RangeField({
  beginControl,
  endControl,
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
  ...props
}) {
  console.log('point');
  console.log(arguments);
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

  const styleHelper =
    labelWidthPixels && colLength > 1
      ? {
          maxWidth: `calc(100% - ${labelWidthPixels})`,
        }
      : { width: '100%' };
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
      <div className="n2o-range-field-body">
        <div className="n2o-range-field-start n2o-range-field-item">
          <div style={styleHelper}>
            <div style={flexStyle}>
              <Control
                placeholder={placeholder}
                visible={visible}
                autoFocus={autoFocus}
                value={value}
                onBlur={onBlur}
                onFocus={onFocus}
                onChange={onChange}
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
        </div>
        <div className="n2o-range-field-end n2o-range-field-item">
          <div style={flexStyle}>
            <Control
              placeholder={placeholder}
              visible={visible}
              autoFocus={autoFocus}
              value={value}
              onBlur={onBlur}
              onFocus={onFocus}
              onChange={onChange}
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
  help: PropTypes.oneOf(PropTypes.string, PropTypes.node),
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
};

export default RangeField;
