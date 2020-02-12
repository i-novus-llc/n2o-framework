import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import isBoolean from 'lodash/isBoolean';
import memoize from 'lodash/memoize';
import some from 'lodash/some';
import omit from 'lodash/omit';
import isEqual from 'lodash/isEqual';
import {
  isInitSelector,
  isVisibleSelector,
  isDisabledSelector,
  messageSelector,
  requiredSelector,
} from '../../../../selectors/formPlugin';
import { registerFieldExtra } from '../../../../actions/formPlugin';
import {
  compose,
  pure,
  withProps,
  defaultProps,
  withHandlers,
  shouldUpdate,
} from 'recompose';
import propsResolver from '../../../../utils/propsResolver';
import { getFormValues } from 'redux-form';

const excludedKeys = [
  'dependencySelector',
  'dispatch',
  'onBlur',
  'onChange',
  'onDragStart',
  'onDrop',
  'onFocus',
  'registerFieldExtra',
  'setReRenderRef',
  'setRef',
  'dirty',
  'pristine',
  'visited',
  'asyncValidating',
  'active',
];

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
        registerFieldExtra,
      } = props;
      !isInit &&
        registerFieldExtra(form, name, {
          visible: visibleToRegister,
          disabled: disabledToRegister,
          dependency,
          required: requiredToRegister,
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
        onBlur,
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

    /**
     * Бозовый рендер
     * @returns {*}
     */
    render() {
      const props = this.props.mapProps(this.props);
      return <Field {...props} />;
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
      model: getFormValues(form)(state),
    };
  };

  const mapDispatchToProps = {
    registerFieldExtra,
  };

  return compose(
    defaultProps({
      disabled: false,
      required: false,
    }),
    withHandlers({
      getValidationState: () => message => {
        if (!message) return;
        if (message.severity === 'success') {
          return 'is-valid';
        } else if (message.severity === 'warning') {
          return 'has-warning';
        }
        return 'is-invalid';
      },
    }),
    withHandlers({
      mapProps: ({ getValidationState }) =>
        memoize(props => {
          if (!props) return;
          const { input, message, meta, model, ...rest } = props;
          const pr = propsResolver(rest, model);
          return {
            ...pr,
            ...meta,
            validationClass: getValidationState(message),
            message,
            ...input,
          };
        }),
    }),
    withProps(props => ({
      visibleToRegister: props.visible,
      disabledToRegister:
        isBoolean(props.enabled) && !props.disabled
          ? !props.enabled
          : props.disabled,
      requiredToRegister: props.required,
    })),
    connect(
      mapStateToProps,
      mapDispatchToProps
    ),
    shouldUpdate((props, nextProps) => {
      const prevResolvedProps = omit(props.mapProps(props), excludedKeys);
      const resolvedProps = omit(props.mapProps(nextProps), excludedKeys);
      const isEqualResolvedProps = some(resolvedProps, (value, key) => {
        return !isEqual(value, prevResolvedProps[key]);
      });
      return isEqualResolvedProps;
    }),
    withProps(props => ({
      ref: props.setReRenderRef,
    })),
    pure
  )(FieldContainer);
};
