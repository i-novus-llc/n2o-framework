import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import { compose, withPropsOnChange } from 'recompose'
import UncontrolledTooltip from 'reactstrap/lib/UncontrolledTooltip'
import omit from 'lodash/omit'
import get from 'lodash/get'
import isNil from 'lodash/isNil'
import { getFormValues } from 'redux-form'
import isUndefined from 'lodash/isUndefined'

import { registerButton } from '../../actions/toolbar'
import {
    isDisabledSelector,
    messageSelector,
    isInitSelector,
    isVisibleSelector,
    countSelector,
} from '../../selectors/toolbar'
import { makeWidgetValidationSelector } from '../../selectors/widgets'
import { validateField } from '../../core/validation/createValidator'
import ModalDialog from '../actions/ModalDialog/ModalDialog'
import { id } from '../../utils/id'
import linkResolver from '../../utils/linkResolver'
import PopoverConfirm from '../snippets/PopoverConfirm/PopoverConfirm'

const ConfirmMode = {
    POPOVER: 'popover',
    MODAL: 'modal',
}

const N2O_ROOT = document.getElementById('n2o')

const RenderTooltip = ({ id, message }) => !isUndefined(message) && (
    <UncontrolledTooltip target={id} boundariesElement={N2O_ROOT}>
        {message}
    </UncontrolledTooltip>
)

export default function withActionButton(options = {}) {
    const { onClick } = options
    const shouldConfirm = !options.noConfirm

    return (WrappedComponent) => {
        class ButtonContainer extends React.Component {
      state = { confirmVisible: false };

      isConfirm = false;

      lastEvent = null;

      constructor(props) {
          super(props)
          this.initIfNeeded()
          this.generatedTooltipId = id()
          this.generatedButtonId = props.uid || id()
      }

      initIfNeeded = () => {
          const {
              isInit,
              entityKey,
              id,
              initialProps: { visible = true, disabled, count, conditions } = {},
              registerButton,
          } = this.props

          !isInit &&
          registerButton(entityKey, id, {
              visible,
              disabled,
              count,
              conditions,
          })
      };

      mapConfirmProps = () => {
          const { confirm } = this.props

          if (confirm) {
              const store = this.context.store.getState()
              const { modelLink, text } = confirm
              const resolvedText = linkResolver(store, {
                  link: modelLink,
                  value: text,
              })

              return {
                  ...confirm,
                  text: resolvedText,
              }
          }
      };

      /**
       * Запуск валидации при клике на кнопку тулбара
       * @param isTouched - отображение ошибок филдов
       * @returns {Promise<*>}
       */
      validationFields = async (isTouched = true) => {
          const { store } = this.context
          const {
              validationConfig,
              validatedWidgetId,
              validate,
              dispatch,
              formValues,
          } = this.props

          if (validate) {
              return await validateField(
                  validationConfig,
                  validatedWidgetId,
                  store.getState(),
                  isTouched,
              )(formValues, dispatch)
          }

          return false
      };

      handleClick = async (e) => {
          e.persist()
          const failValidate = await this.validationFields()
          const { confirm } = this.props
          const state = this.context.store.getState()

          if (!onClick || failValidate) {
              return
          }
          if (confirm && !this.isConfirm && shouldConfirm) {
              this.lastEvent = e
              this.lastEvent.preventDefault()
              this.handleOpenConfirmModal()
          } else {
              this.isConfirm = false
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
                  state,
              )
              this.lastEvent = null
          }
      };

      handleConfirm = (e) => {
          this.isConfirm = true
          this.handleCloseConfirmModal(e, () => {
              this.handleClick(e)
          })
      };

      handleOpenConfirmModal = (cb) => {
          this.setState({ confirmVisible: true }, cb)
      };

      handleCloseConfirmModal = (e, cb) => {
          const defaultCb = () => (this.lastEvent = null)

          this.setState({ confirmVisible: false }, cb || defaultCb)
      };

      render() {
          const { confirm, hint, message } = this.props
          const { confirmVisible } = this.state

          const confirmMode = get(confirm, 'mode')

          const visible = !isNil(this.props.visible)
              ? this.props.visible
              : this.props.visibleFromState

          const disabled = !isNil(this.props.disabled)
              ? this.props.disabled
              : this.props.disabledFromState

          const currentMessage = disabled ? message || hint : hint

          return (
              <div id={this.generatedTooltipId}>
                  <RenderTooltip
                      message={currentMessage}
                      id={this.generatedTooltipId}
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
                      disabled={disabled}
                      visible={visible}
                      onClick={this.handleClick}
                      id={this.generatedButtonId}
                  />
                  {confirmMode === ConfirmMode.POPOVER ? (
                      <PopoverConfirm
                          {...this.mapConfirmProps(confirm)}
                          isOpen={confirmVisible}
                          onConfirm={this.handleConfirm}
                          onDeny={this.handleCloseConfirmModal}
                          target={this.generatedButtonId}
                      />
                  ) : confirmMode === ConfirmMode.MODAL ? (
                      <ModalDialog
                          {...this.mapConfirmProps(confirm)}
                          visible={confirmVisible}
                          onConfirm={this.handleConfirm}
                          onDeny={this.handleCloseConfirmModal}
                          close={this.handleCloseConfirmModal}
                      />
                  ) : null}
              </div>
          )
      }
        }

        const mapStateToProps = createStructuredSelector({
            isInit: (state, ownProps) => isInitSelector(ownProps.entityKey, ownProps.id)(state),
            visibleFromState: (state, ownProps) => isVisibleSelector(ownProps.entityKey, ownProps.id)(state),
            disabledFromState: (state, ownProps) => isDisabledSelector(ownProps.entityKey, ownProps.id)(state),
            message: (state, ownProps) => messageSelector(ownProps.entityKey, ownProps.id)(state),
            count: (state, ownProps) => countSelector(ownProps.entityKey, ownProps.id)(state),
            validationConfig: (state, ownProps) => makeWidgetValidationSelector(ownProps.validatedWidgetId)(state),
            formValues: (state, ownProps) => getFormValues(ownProps.validatedWidgetId)(state),
        })

        function mapDispatchToProps(dispatch) {
            return {
                dispatch,
                registerButton: (entityKey, id, initialProps) => {
                    dispatch(registerButton(entityKey, id, initialProps))
                },
            }
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
        }

        ButtonContainer.contextTypes = {
            store: PropTypes.object,
        }

        return compose(
            withPropsOnChange(
                ['visible', 'disabled', 'count', 'conditions'],
                ({ visible, disabled, count, conditions }) => ({
                    initialProps: { visible, disabled, count, conditions },
                }),
            ),
            connect(
                mapStateToProps,
                mapDispatchToProps,
            ),
        )(ButtonContainer)
    }
}
