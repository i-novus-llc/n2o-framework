import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { compose, withPropsOnChange } from 'recompose'
import omit from 'lodash/omit'
import get from 'lodash/get'
import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'

import { SimpleTooltip } from '../snippets/Tooltip/SimpleTooltip'
import { registerButton, removeButton } from '../../ducks/toolbar/store'
import {
    toolbarSelector,
    isDisabledSelector,
    messageSelector,
    isInitSelector,
    isVisibleSelector,
    countSelector,
} from '../../ducks/toolbar/selectors'
import { validate as validateForm } from '../../core/validation/createValidator'
import ModalDialog from '../actions/ModalDialog/ModalDialog'
import { id as getID } from '../../utils/id'
import linkResolver from '../../utils/linkResolver'
import { PopoverConfirm } from '../snippets/PopoverConfirm/PopoverConfirm'

const ConfirmMode = {
    POPOVER: 'popover',
    MODAL: 'modal',
}

const validatePage = (
    pageId,
    store,
    isTouched,
    dispatch,
) => {
    const state = store.getState()
    const page = pageId === '_' ? '' : pageId

    const promises = Object.keys(state.form || {})
        .filter(formName => formName.startsWith(page))
        .map((formId) => {
            const validation = get(state, ['widgets', formId, 'validation'])
            const form = state.form[formId]
            const { registeredFields, values } = form

            if (
                // Если для формы нету registeredFields или он пустой, то форма была на закрытой модалке - валидировать уже нечего
                !registeredFields || isEmpty(registeredFields) ||
                isEmpty(validation)
            ) { return Promise.resolve(false) }

            return validateForm(validation, formId, state, isTouched, values, dispatch)
        })

    if (!promises.length) {
        return Promise.resolve(false)
    }

    return Promise.all(promises).then(results => results.some(hasError => hasError))
}

/*
 * TODO декомпозировать ХОК
 *  вынести отдельно части, отвечающие за
 *  - вызов регистрации/удаления кнопок из стора
 *  - рендер
 *  - вызов валидации
 */
export default function withActionButton(options = {}) {
    const { onClick } = options
    const shouldConfirm = !options.noConfirm

    return (WrappedComponent) => {
        class ButtonContainer extends React.Component {
            state = { confirmVisible: false }

            isConfirm = false

            lastEvent = null

            constructor(props) {
                super(props)
                this.generatedTooltipId = getID()
                this.generatedButtonId = props.uid || getID()
            }

            componentWillUnmount() {
                const { removeButton, entityKey, id } = this.props

                removeButton(entityKey, id)
            }

            componentDidMount() {
                this.initIfNeeded()
            }

            initIfNeeded = () => {
                const {
                    entityKey,
                    id,
                    initialProps: {
                        visible = true,
                        disabled,
                        count,
                        conditions,
                    } = {},
                    registerButton,
                } = this.props

                registerButton(entityKey, id, { visible, disabled, count, conditions })
            }

            // eslint-disable-next-line consistent-return
            mapConfirmProps = () => {
                const { confirm } = this.props

                if (confirm) {
                    const { store } = this.context
                    const state = store.getState()
                    const { modelLink, text } = confirm
                    const resolvedText = linkResolver(state, { link: modelLink, value: text })

                    return { ...confirm, text: resolvedText }
                }
            }

      /**
       * Запуск валидации при клике на кнопку тулбара
       * @param isTouched - отображение ошибок филдов
       * @returns {Promise<*>}
       */
      validationFields = async (isTouched = true) => {
          const { store } = this.context
          const {
              validate,
              dispatch,
          } = this.props

          switch (validate) {
              case 'widget': {
                  const { validateWidgetId } = this.props
                  const state = store.getState()

                  return validateForm(
                      get(state, ['widgets', validateWidgetId, 'validation']),
                      validateWidgetId,
                      store.getState(),
                      isTouched,
                      get(state, ['form', validateWidgetId, 'values']),
                      dispatch,
                  )
              }
              case 'page': {
                  const { validatePageId } = this.props

                  return validatePage(validatePageId, store, isTouched, dispatch)
              }
              default: {
                  return false
              }
          }
      };

            handleClick = async (e) => {
                e.persist()
                const failValidate = await this.validationFields()
                const { confirm } = this.props
                const { store } = this.context
                const state = store.getState()

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
            }

            handleConfirm = (e) => {
                this.isConfirm = true
                this.handleCloseConfirmModal(e, () => {
                    this.handleClick(e)
                })
            }

            handleOpenConfirmModal = (cb) => {
                this.setState({ confirmVisible: true }, cb)
            }

            handleCloseConfirmModal = (e, cb) => {
                // eslint-disable-next-line no-return-assign
                const defaultCb = () => (this.lastEvent = null)

                this.setState({ confirmVisible: false }, cb || defaultCb)
            }

            render() {
                const {
                    confirm,
                    hint,
                    message,
                    visible,
                    visibleFromState,
                    disabled,
                    disabledFromState,
                } = this.props
                const { confirmVisible } = this.state
                const confirmMode = get(confirm, 'mode')

                const currentVisible = !isNil(visible)
                    ? visible
                    : visibleFromState

                const currentDisabled = !isNil(disabled)
                    ? disabled
                    : disabledFromState

                const currentMessage = currentDisabled ? message || hint : hint

                return (
                    <div id={this.generatedTooltipId}>
                        <SimpleTooltip
                            id={this.generatedTooltipId}
                            message={currentMessage}
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
                            disabled={currentDisabled}
                            visible={currentVisible}
                            onClick={this.handleClick}
                            id={this.generatedButtonId}
                        />
                        {
                            confirmMode === ConfirmMode.POPOVER
                                ? (
                                    <PopoverConfirm
                                        {...this.mapConfirmProps(confirm)}
                                        isOpen={confirmVisible}
                                        onConfirm={this.handleConfirm}
                                        onDeny={this.handleCloseConfirmModal}
                                        target={this.generatedButtonId}
                                    />
                                )
                                : null
                        }
                        {
                            confirmMode === ConfirmMode.MODAL
                                ? (
                                    <ModalDialog
                                        {...this.mapConfirmProps(confirm)}
                                        visible={confirmVisible}
                                        onConfirm={this.handleConfirm}
                                        onDeny={this.handleCloseConfirmModal}
                                        close={this.handleCloseConfirmModal}
                                    />
                                )
                                : null
                        }
                    </div>
                )
            }
        }

        const mapStateToProps = (state, { entityKey, id, validatedWidgetId }) => ({
            isInit: isInitSelector(state, entityKey, id),
            visibleFromState: isVisibleSelector(state, entityKey, id),
            disabledFromState: isDisabledSelector(state, entityKey, id),
            message: messageSelector(state, entityKey, id),
            count: countSelector(state, entityKey, id),
            validationConfig: makeWidgetValidationSelector(validatedWidgetId)(state),
            formValues: getFormValues(validatedWidgetId)(state),
            toolbar: toolbarSelector(state),
        })

        function mapDispatchToProps(dispatch) {
            return {
                dispatch,
                registerButton: (entityKey, id, initialProps) => {
                    dispatch(registerButton(entityKey, id, initialProps))
                },
                removeButton: (entityKey, id) => {
                    dispatch(removeButton(entityKey, id))
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
            uid: PropTypes.string,
            entityKey: PropTypes.string,
            id: PropTypes.string,
            validateWidgetId: PropTypes.string,
            validatePageId: PropTypes.string,
            validate: PropTypes.oneOf(['page', 'widget', 'none']),
            confirm: PropTypes.object,
            registerButton: PropTypes.func,
            removeButton: PropTypes.func,
            dispatch: PropTypes.func,
            confirmMode: PropTypes.string,
            message: PropTypes.oneOf(['string', null, undefined]),
            toolbar: PropTypes.object,
            visibleFromState: PropTypes.bool,
            disabledFromState: PropTypes.bool,
        }

        ButtonContainer.contextTypes = {
            store: PropTypes.object,
        }

        return compose(
            withPropsOnChange(
                ['visible', 'disabled', 'count', 'conditions', 'subMenu'],
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
