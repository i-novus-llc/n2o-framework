import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { compose, withPropsOnChange } from 'recompose'
import omit from 'lodash/omit'
import get from 'lodash/get'
import isNil from 'lodash/isNil'
import isEmpty from 'lodash/isEmpty'

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
import { validate as validateDatasource } from '../../core/datasource/validate'
import { MODEL_PREFIX } from '../../core/datasource/const'
import ModalDialog from '../actions/ModalDialog/ModalDialog'
import { id as getID } from '../../utils/id'
import linkResolver from '../../utils/linkResolver'
import { PopoverConfirm } from '../snippets/PopoverConfirm/PopoverConfirm'
import evalExpression, { parseExpression } from '../../utils/evalExpression'

const ConfirmMode = {
    POPOVER: 'popover',
    MODAL: 'modal',
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
            constructor(props) {
                super(props)
                this.generatedTooltipId = getID()
                this.generatedButtonId = props.uid || getID()

                this.state = {
                    confirmVisible: false,
                    permittedUrl: props.url,
                }
            }

            isConfirm = false

            lastEvent = null

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
                    const { modelLink, text, condition } = confirm
                    const model = get(state, modelLink)

                    const resolvedText = linkResolver(state, { link: modelLink, value: text })

                    const getResolvedCondition = (condition) => {
                        if (isEmpty(condition)) {
                            return true
                        }

                        if (typeof condition === 'boolean') {
                            return condition
                        }

                        return evalExpression(parseExpression(condition), model)
                    }

                    return {
                        ...confirm,
                        text: resolvedText,
                        resolvedConditions: getResolvedCondition(condition),
                    }
                }
            }

            /**
            * Запуск валидации при клике на кнопку тулбара
            * @param isTouched - отображение ошибок филдов
            * @returns {Promise<*>}
            */
            validationFields = async () => {
                const { store } = this.context
                const {
                    validate,
                    dispatch,
                } = this.props

                if (!validate?.length) {
                    return true
                }

                let valid = true

                for (const datasourceId of validate) {
                    const isDatasourceValid = await validateDatasource(
                        store.getState(),
                        datasourceId,
                        MODEL_PREFIX.active,
                        dispatch,
                        true,
                    )

                    valid = valid && isDatasourceValid
                }

                return valid
            }

            handleClick = async (e) => {
                e.persist()

                const valid = await this.validationFields()

                const { confirm, url } = this.props
                const { store } = this.context
                const state = store.getState()

                if (!onClick || !valid) {
                    this.setState({
                        permittedUrl: null,
                    })

                    return
                }

                this.setState({
                    permittedUrl: url,
                })

                const resolvedConfirmProps = this.mapConfirmProps(confirm) || {}
                const { resolvedConditions } = resolvedConfirmProps

                if (confirm && !this.isConfirm && shouldConfirm && resolvedConditions) {
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
                    hintPosition,
                } = this.props

                const { confirmVisible, permittedUrl } = this.state

                const confirmMode = get(confirm, 'mode')

                const currentVisible = !isNil(visible)
                    ? visible
                    : visibleFromState

                const currentDisabled = !isNil(disabled)
                    ? disabled
                    : disabledFromState

                const currentMessage = currentDisabled ? message || hint : hint

                const resolvedConfirmProps = this.mapConfirmProps(confirm)

                return (
                    <div id={this.generatedTooltipId}>
                        <SimpleTooltip
                            id={this.generatedTooltipId}
                            message={currentMessage}
                            placement={hintPosition}
                        />
                        <WrappedComponent
                            {...omit(this.props, [
                                'isInit',
                                'targetTooltip',
                                'initialProps',
                                'registerButton',
                                'uid',
                            ])}
                            url={permittedUrl}
                            disabled={currentDisabled}
                            visible={currentVisible}
                            onClick={this.handleClick}
                            id={this.generatedButtonId}
                        />
                        {
                            confirmMode === ConfirmMode.POPOVER
                                ? (
                                    <PopoverConfirm
                                        {...resolvedConfirmProps}
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
                                        {...resolvedConfirmProps}
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

        const mapStateToProps = (state, { entityKey, id }) => ({
            isInit: isInitSelector(state, entityKey, id),
            visibleFromState: isVisibleSelector(state, entityKey, id),
            disabledFromState: isDisabledSelector(state, entityKey, id),
            message: messageSelector(state, entityKey, id),
            count: countSelector(state, entityKey, id),
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
            validate: PropTypes.arrayOf(PropTypes.string),
            confirm: PropTypes.object,
            registerButton: PropTypes.func,
            removeButton: PropTypes.func,
            dispatch: PropTypes.func,
            confirmMode: PropTypes.string,
            message: PropTypes.oneOf(['string', null, undefined]),
            toolbar: PropTypes.object,
            visibleFromState: PropTypes.bool,
            disabledFromState: PropTypes.bool,
            hintPosition: PropTypes.oneOf(['left', 'top', 'right', 'bottom']),
            url: PropTypes.string,
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
