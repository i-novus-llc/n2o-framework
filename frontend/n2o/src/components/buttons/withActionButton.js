import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { createStructuredSelector } from 'reselect';
import { compose, withPropsOnChange } from 'recompose';
import { omit } from 'lodash';

import { registerButton } from '../../actions/toolbar';
import {
  isDisabledSelector,
  isInitSelector,
  isVisibleSelector,
  countSelector
} from '../../selectors/toolbar';
import { getFormValues } from 'redux-form';
import { makeWidgetValidationSelector } from '../../selectors/widgets';
import { validateField } from '../../core/validation/createValidator';

import ModalDialog from '../actions/ModalDialog/ModalDialog';
import Tooltip from '../snippets/Tooltip/Tooltip';
import { id } from '../../utils/id';
import linkResolver from '../../utils/linkResolver';

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
          initialProps: { visible = true, disabled = false, count, conditions } = {},
          registerButton
        } = this.props;
        !isInit &&
          registerButton(entityKey, id, {
            visible,
            disabled,
            count,
            conditions
          });
      };

      mapConfirmProps = () => {
        const { confirm } = this.props;
        if (confirm) {
          const store = this.context.store.getState();
          const { modelLink, text } = confirm;
          const resolvedText = linkResolver(store, {
            link: modelLink,
            value: text
          });
          return {
            ...confirm,
            text: resolvedText
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
        const { validationConfig, entityKey, validate, dispatch, formValues } = this.props;

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

        if (!onClick || failValidate) {
          return;
        }
        if (confirm && !this.isConfirm && shouldConfirm) {
          this.lastEvent = e;
          this.lastEvent.preventDefault();
          this.handleOpenConfirmModal();
        } else {
          this.isConfirm = false;
          onClick(this.lastEvent || e, {
            ...omit(this.props, [
              'isInit',
              'initialProps',
              'registerButton',
              'uid',
              'validationConfig',
              'formValues'
            ]),
            isConfirm: this.isConfirm
          });
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
        const { confirm, hint } = this.props;
        const { confirmVisible } = this.state;
        return (
          <React.Fragment>
            <Tooltip target={this.generatedButtonId} hint={hint}>
              <WrappedComponent
                {...omit(this.props, [
                  'isInit',
                  'targetTooltip',
                  'initialProps',
                  'registerButton',
                  'uid',
                  'validationConfig',
                  'formValues'
                ])}
                onClick={this.handleClick}
                id={this.generatedButtonId}
              />
            </Tooltip>
            {confirm && (
              <ModalDialog
                {...this.mapConfirmProps(confirm)}
                visible={confirmVisible}
                onConfirm={this.handleConfirm}
                onDeny={this.handleCloseConfirmModal}
                close={this.handleCloseConfirmModal}
              />
            )}
          </React.Fragment>
        );
      }
    }

    const mapStateToProps = createStructuredSelector({
      isInit: (state, ownProps) => isInitSelector(ownProps.entityKey, ownProps.id)(state),
      visible: (state, ownProps) => isVisibleSelector(ownProps.entityKey, ownProps.id)(state),
      disabled: (state, ownProps) => isDisabledSelector(ownProps.entityKey, ownProps.id)(state),
      count: (state, ownProps) => countSelector(ownProps.entityKey, ownProps.id)(state),
      validationConfig: (state, ownProps) =>
        makeWidgetValidationSelector(ownProps.entityKey)(state),
      formValues: (state, ownProps) => getFormValues(ownProps.entityKey)(state)
    });

    function mapDispatchToProps(dispatch) {
      return {
        dispatch,
        registerButton: (entityKey, id, initialProps) => {
          dispatch(registerButton(entityKey, id, initialProps));
        }
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
      style: PropTypes.object
    };

    ButtonContainer.defaultProps = {
      visible: true,
      disabled: false
    };

    ButtonContainer.contextTypes = {
      store: PropTypes.object
    };

    return compose(
      withPropsOnChange(
        ['visible', 'disabled', 'count', 'conditions'],
        ({ visible, disabled, count, conditions }) => {
          return {
            initialProps: { visible, disabled, count, conditions }
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
