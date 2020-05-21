import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { createStructuredSelector } from 'reselect';
import { compose, withPropsOnChange } from 'recompose';
import UncontrolledTooltip from 'reactstrap/lib/UncontrolledTooltip';
import omit from 'lodash/omit';

import isUndefined from 'lodash/isUndefined';

import { registerButton } from '../../actions/toolbar';
import {
  isDisabledSelector,
  messageSelector,
  isInitSelector,
  isVisibleSelector,
  countSelector,
} from '../../selectors/toolbar';
import { getFormValues } from 'redux-form';
import { makeWidgetValidationSelector } from '../../selectors/widgets';
import { validateField } from '../../core/validation/createValidator';

import ModalDialog from '../actions/ModalDialog/ModalDialog';
import { id } from '../../utils/id';
import linkResolver from '../../utils/linkResolver';

const RenderTooltip = ({ id, message }) => {
  return (
    !isUndefined(message) && (
      <UncontrolledTooltip target={id}>{message}</UncontrolledTooltip>
    )
  );
};

export default function withActionButton(options = {}) {
  const onClick = options.onClick;
  const shouldConfirm = !options.noConfirm;
  return WrappedComponent => {
    class ButtonContainer extends React.Component {
      state = { confirmVisible: false };
      isConfirm = false;
      lastEvent = null;

      constructor(props) {
        super(props);
        this.initIfNeeded();
        this.generatedButtonId = props.uid || id();
      }

      initIfNeeded = () => {
        const {
          isInit,
          entityKey,
          id,
          initialProps: {
            visible = true,
            disabled = false,
            count,
            conditions,
          } = {},
          registerButton,
        } = this.props;
        !isInit &&
          registerButton(entityKey, id, {
            visible,
            disabled,
            count,
            conditions,
          });
      };

      mapConfirmProps = () => {
        const { confirm } = this.props;
        if (confirm) {
          const store = this.context.store.getState();
          const { modelLink, text } = confirm;
          const resolvedText = linkResolver(store, {
            link: modelLink,
            value: text,
          });
          return {
            ...confirm,
            text: resolvedText,
          };
        }
      };

      /**
       * Запуск валидации при клике на кнопку тулбара
       * @param isTouched - отображение ошибок филдов
       * @returns {Promise<*>}
       */
      validationFields = async (isTouched = true) => {
        const { store } = this.context;
        const {
          validationConfig,
          entityKey,
          validate,
          dispatch,
          formValues,
        } = this.props;

        if (validate) {
          const errors = await validateField(
            validationConfig,
            entityKey,
            store.getState(),
            isTouched
          )(formValues, dispatch);

          return errors;
        }
        return false;
      };

      handleClick = async e => {
        e.persist();
        const failValidate = await this.validationFields();
        const { confirm } = this.props;
        const state = this.context.store.getState();

        if (!onClick || failValidate) {
          return;
        }
        if (confirm && !this.isConfirm && shouldConfirm) {
          this.lastEvent = e;
          this.lastEvent.preventDefault();
          this.handleOpenConfirmModal();
        } else {
          this.isConfirm = false;
          onClick(
            this.lastEvent || e,
            {
              ...omit(this.props, [
                'isInit',
                'initialProps',
                'registerButton',
                'uid',
                'validationConfig',
                'formValues',
              ]),
              isConfirm: this.isConfirm,
            },
            state
          );
          this.lastEvent = null;
        }
      };

      handleConfirm = e => {
        this.isConfirm = true;
        this.handleCloseConfirmModal(e, () => {
          this.handleClick(e);
        });
      };

      handleOpenConfirmModal = cb => {
        this.setState({ confirmVisible: true }, cb);
      };

      handleCloseConfirmModal = (e, cb) => {
        const defaultCb = () => (this.lastEvent = null);
        this.setState({ confirmVisible: false }, cb || defaultCb);
      };

      render() {
        const { confirm, hint, disabled, message } = this.props;
        const { confirmVisible } = this.state;

        const currentMessage = disabled ? message : hint;

        return (
          <div id={this.generatedButtonId}>
            <RenderTooltip
              message={currentMessage}
              id={this.generatedButtonId}
            />
            <WrappedComponent
              {...omit(this.props, [
                'isInit',
                'targetTooltip',
                'initialProps',
                'registerButton',
                'uid',
                'validationConfig',
                'formValues',
              ])}
              onClick={this.handleClick}
              id={this.generatedButtonId}
            />
            {confirm && (
              <ModalDialog
                {...this.mapConfirmProps(confirm)}
                visible={confirmVisible}
                onConfirm={this.handleConfirm}
                onDeny={this.handleCloseConfirmModal}
                close={this.handleCloseConfirmModal}
              />
            )}
          </div>
        );
      }
    }

    const mapStateToProps = createStructuredSelector({
      isInit: (state, ownProps) =>
        isInitSelector(ownProps.entityKey, ownProps.id)(state),
      visible: (state, ownProps) =>
        isVisibleSelector(ownProps.entityKey, ownProps.id)(state),
      disabled: (state, ownProps) =>
        isDisabledSelector(ownProps.entityKey, ownProps.id)(state),
      message: (state, ownProps) =>
        messageSelector(ownProps.entityKey, ownProps.id)(state),
      count: (state, ownProps) =>
        countSelector(ownProps.entityKey, ownProps.id)(state),
      validationConfig: (state, ownProps) =>
        makeWidgetValidationSelector(ownProps.entityKey)(state),
      formValues: (state, ownProps) => getFormValues(ownProps.entityKey)(state),
    });

    function mapDispatchToProps(dispatch) {
      return {
        dispatch,
        registerButton: (entityKey, id, initialProps) => {
          dispatch(registerButton(entityKey, id, initialProps));
        },
      };
    }

    ButtonContainer.propTypes = {
      isInit: PropTypes.bool,
      visible: PropTypes.bool,
      disabled: PropTypes.bool,
      count: PropTypes.number,
      initialProps: PropTypes.object,
      hint: PropTypes.string,
      className: PropTypes.string,
      style: PropTypes.object,
    };

    ButtonContainer.defaultProps = {
      visible: true,
      disabled: false,
    };

    ButtonContainer.contextTypes = {
      store: PropTypes.object,
    };

    return compose(
      withPropsOnChange(
        ['visible', 'disabled', 'count', 'conditions'],
        ({ visible, disabled, count, conditions }) => {
          return {
            initialProps: { visible, disabled, count, conditions },
          };
        }
      ),
      connect(
        mapStateToProps,
        mapDispatchToProps
      )
    )(ButtonContainer);
  };
}
