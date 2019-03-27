import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { isBoolean } from 'lodash';
import {
  isInitSelector,
  isVisibleSelector,
  isDisabledSelector,
  messageSelector,
  requiredSelector
} from '../../../../selectors/formPlugin';
import { registerFieldExtra } from '../../../../actions/formPlugin';
import { compose, pure, withProps, defaultProps } from 'recompose';
import propsResolver from '../../../../utils/propsResolver';
import { getFormValues } from 'redux-form';

/**
 * HOC обертка для полей, в которой содержится мэппинг свойств редакса и регистрация дополнительных свойств полей
 * @param Field
 * @returns {*}
 */
export default Field => {
  class FieldContainer extends React.Component {
    constructor(props) {
      super(props);
      this.onChange = this.onChange.bind(this);
      this.onFocus = this.onFocus.bind(this);
      this.onBlur = this.onBlur.bind(this);
      this.initIfNeeded(props);
    }

    /**
     * Регистрация дополнительных свойств поля
     */
    initIfNeeded(props) {
      const {
        meta: { form },
        input: { name },
        isInit,
        visibleToRegister,
        disabledToRegister,
        dependency,
        requiredToRegister,
        registerFieldExtra
      } = props;
      !isInit &&
        registerFieldExtra(form, name, {
          visible: visibleToRegister,
          disabled: disabledToRegister,
          dependency,
          required: requiredToRegister
        });
    }

    /**
     * мэппинг onChange
     * @param e
     */
    onChange(e) {
      const { input, onChange } = this.props;
      input && input.onChange(e);
      onChange && onChange(e);
    }

    /**
     * мэппинг onBlur
     * @param e
     */
    onBlur(e) {
      const {
        meta: { form },
        input: { name },
        value,
        input,
        onBlur
      } = this.props;
      input && input.onBlur(e);
      onBlur && onBlur(e.target.value);
    }

    /**
     * мэппинг onFocus
     * @param e
     */
    onFocus(e) {
      const { input, onFocus } = this.props;
      input && input.onFocus(e);
      onFocus && onFocus(e.target.value);
    }

    /**
     * мэппинг сообщений
     * @param error
     * @returns {string}
     */
    getValidationState(message) {
      if (!message) return;
      if (message.severity === 'success') {
        return 'is-valid';
      } else if (message.severity === 'warning') {
        return 'has-warning';
      }
      return 'is-invalid';
    }

    /**
     * общий мэппинг
     * @returns {{validationClass: string, value: *, onChange: FieldContainer.onChange, onFocus: FieldContainer.onFocus, onBlur: FieldContainer.onBlur}}
     */
    mapProps() {
      const { input, message, meta, model, ...rest } = this.props;
      return {
        ...propsResolver(rest, model),
        ...meta,
        validationClass: this.getValidationState(message),
        message,
        ...input
      };
    }

    /**
     * Бозовый рендер
     * @returns {*}
     */
    render() {
      return <Field {...this.mapProps()} />;
    }
  }

  const mapStateToProps = (state, ownProps) => {
    const { form } = ownProps.meta;
    const { name } = ownProps.input;
    return {
      isInit: isInitSelector(form, name)(state),
      visible: isVisibleSelector(form, name)(state),
      disabled: isDisabledSelector(form, name)(state),
      message: messageSelector(form, name)(state),
      required: requiredSelector(form, name)(state),
      model: getFormValues(form)(state)
    };
  };

  const mapDispatchToProps = {
    registerFieldExtra
  };

  return compose(
    defaultProps({
      visible: true,
      disabled: false,
      required: false
    }),
    withProps(props => ({
      visibleToRegister: props.visible,
      disabledToRegister:
        isBoolean(props.enabled) && !props.disabled ? !props.enabled : props.disabled,
      requiredToRegister: props.required
    })),
    connect(
      mapStateToProps,
      mapDispatchToProps
    ),
    withProps(props => ({
      ref: props.setReRenderRef
    })),
    pure
  )(FieldContainer);
};
