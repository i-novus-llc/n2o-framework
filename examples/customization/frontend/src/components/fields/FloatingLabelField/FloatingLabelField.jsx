import React from "react";
import PropTypes from "prop-types";
import cx from "classnames";
import { FormGroup, Label } from "reactstrap";

import Control from "n2o/lib/components/widgets/Form/fields/StandardField/Control";

import "./FloatingLabel.scss";

class FloatingLabelField extends React.Component {
  state = {
    focused: false
  };

  handleFocus = e => {
    this.setState({
      focused: true
    });
    this.control && this.control.focus && this.control.focus();
  };

  handleBlur = e => {
    this.setState({
      focused: false
    });
  };

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
    const flexStyle = { display: "flex" };
    const validationMap = {
      "is-valid": "text-success",
      "is-invalid": "text-danger",
      "has-warning": "text-warning"
    };
    const styleHelper = { width: "100%" };
    const labelWidthPixels =
      labelWidth === "default"
        ? 180
        : labelWidth === "min"
        ? undefined
        : labelWidth;
    const extendedLabelStyle = {
      width: labelWidthPixels,
      flex: labelWidthPixels ? "none" : undefined,
      ...labelStyle
    };
    return (
      visible && (
        <div className="floating-labels" onClick={this.handleFocus}>
          <FormGroup className={cx({ focused: this.state.focused, filled: value })}>
            <Control
              inputRef={el => (this.control = el)}
              visible={visible}
              autoFocus={autoFocus}
              value={value}
              onBlur={this.handleBlur}
              onFocus={onFocus}
              onChange={onChange}
              {...control}
              {...props}
              className={cx(control && control.className, {
                [validationClass]: touched
              })}
            />
            <span className="bar"></span>
            <Label>{label}</Label>
          </FormGroup>
        </div>
      )
    );
  }
}

FloatingLabelField.propTypes = {
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
  valiastionClass: PropTypes.string,
  loading: PropTypes.bool,
  touched: PropTypes.bool,
  labelWidth: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  labelAlignment: PropTypes.oneOf(["left", "right"]),
  labelPosition: PropTypes.oneOf(["top-left", "top-right", "left", "right"]),
  message: PropTypes.object,
  help: PropTypes.oneOf(PropTypes.string, PropTypes.node)
};

FloatingLabelField.defaultProps = {
  visible: true,
  required: false,
  control: <input />,
  loading: false,
  className: "",
  style: {},
  enabled: true,
  disabled: false,
  onChange: () => {}
};

export default FloatingLabelField;
