import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { isBoolean } from 'lodash';
import {
  isInitSelector,
  isVisibleSelector,
  isDisabledSelector,
  messageSelector,
  filterSelector,
  requiredSelector
} from '../../../../selectors/formPlugin';
import { registerFieldExtra } from '../../../../actions/formPlugin';
import { compose, pure, withProps, defaultProps } from 'recompose';
import propsResolver from '../../../../utils/propsResolver';

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
        visible,
        disabled,
        dependency,
        registerFieldExtra,
        requiredToRegister,
        required
      } = props;
      if (!isInit) {
        registerFieldExtra(form, name, {
          visible,
          disabled,
          dependency,
          required: isBoolean(requiredToRegister) ? requiredToRegister : required
        });
      }
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
      const { input, value, message, meta } = this.props;
      return {
        ...propsResolver(this.props, this.context._reduxForm.getValues()),
        ...meta,
        validationClass: this.getValidationState(message),
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

  FieldContainer.contextTypes = {
    _reduxForm: PropTypes.object
  };

  const mapStateToProps = (state, ownProps) => {
    const { form } = ownProps.meta;
    const { name } = ownProps.input;
    const isVisible = isVisibleSelector(form, name)(state);
    const isDisabled = isDisabledSelector(form, name)(state);
    const isRequired = requiredSelector(form, name)(state);
    return {
      isInit: isInitSelector(form, name)(state),
      visible: isBoolean(isVisible) ? isVisible : ownProps.visible,
      disabled: isBoolean(isDisabled) ? isDisabled : ownProps.disabled,
      message: messageSelector(form, name)(state),
      required: isBoolean(isRequired) ? isRequired : ownProps.required,
      filterValues: filterSelector(form, name)(state)
    };
  };

  const mapDispatchToProps = {
    registerFieldExtra
  };

  return compose(
    withProps(props => ({
      requiredToRegister: props.required
    })),
    connect(
      mapStateToProps,
      mapDispatchToProps
    ),
    withProps(props => ({
      disabled: isBoolean(props.enabled) && !props.disabled ? !props.enabled : props.disabled,
      ref: props.setReRenderRef
    })),
    defaultProps({
      isInit: false,
      visible: true,
      disabled: false
    }),
    pure
  )(FieldContainer);
};
