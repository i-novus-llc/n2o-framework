import React from 'react'
import PropTypes from 'prop-types'
import { connect, ReactReduxContext } from 'react-redux'
import { compose, withPropsOnChange } from 'recompose'
import omit from 'lodash/omit'
import get from 'lodash/get'
import isNil from 'lodash/isNil'
import isEmpty from 'lodash/isEmpty'
import isObject from 'lodash/isObject'

import { registerButton, removeButton } from '../../ducks/toolbar/store'
import {
    toolbarSelector,
    isDisabledSelector,
    messageSelector,
    isInitSelector,
    isVisibleSelector,
    countSelector,
} from '../../ducks/toolbar/selectors'
import { validate as validateDatasource } from '../../core/validation/validate'
import { id as getID } from '../../utils/id'
import linkResolver from '../../utils/linkResolver'
import evalExpression, { parseExpression } from '../../utils/evalExpression'
import { ModelPrefix } from '../../core/datasource/const'
import { getModelByPrefixAndNameSelector } from '../../ducks/models/selectors'
import propsResolver from '../../utils/propsResolver'

import { ActionButton } from './ActionButton'

/*
 * TODO декомпозировать ХОК
 *  вынести отдельно части, отвечающие за
 *  - вызов регистрации/удаления кнопок из стора
 *  - рендер
 *  - вызов валидации
 */
export default function withActionButton(options = {}) {
    const { onClick } = options

    return (WrappedComponent) => {
        class ButtonContainer extends React.Component {
            constructor(props) {
                super(props)
                this.generatedTooltipId = getID()

                this.state = {
                    id: props.uid || getID(),
                    confirm: null,
                    event: null,
                    url: props.url,
                }
            }

            isConfirm = false

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

            mapConfirmProps = () => {
                const { confirm: confirmJSON } = this.props

                if (isEmpty(confirmJSON)) { return null }

                const { store } = this.context
                const state = store.getState()
                const { modelLink, text, condition, ...confirm } = confirmJSON
                const model = get(state, modelLink)

                const resolvedText = linkResolver(state, { link: modelLink, value: text })

                const getResolvedCondition = (condition) => {
                    if (isObject(condition) && isEmpty(condition)) {
                        return true
                    }

                    if (typeof condition === 'boolean') {
                        return condition
                    }

                    return evalExpression(parseExpression(condition), model)
                }

                if (!getResolvedCondition(condition)) {
                    return null
                }

                return {
                    ...confirm,
                    text: resolvedText,
                }
            }

            /**
             * Запуск валидации при клике на кнопку тулбара
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
                        ModelPrefix.active,
                        dispatch,
                        true,
                    )

                    valid = valid && isDatasourceValid
                }

                return valid
            }

            onClickHandler = async (event) => {
                event.persist()

                const valid = await this.validationFields()

                if (!valid) {
                    event.preventDefault()
                    this.setState({
                        url: null,
                    })

                    return
                }

                const confirm = this.mapConfirmProps()

                if (!confirm) {
                    const { store } = this.context
                    const state = store.getState()

                    onClick(event, this.props, state)

                    return
                }

                event.preventDefault()
                this.setState({ confirm, event })
            }

            onConfirm = () => {
                const { event } = this.state
                const { store } = this.context
                const { url } = this.props
                const state = store.getState()

                this.setState({ confirm: null, event: null, url })
                onClick(event, this.props, state)
            }

            onDeny = () => {
                this.setState({ confirm: null, event: null })
            }

            render() {
                const {
                    hint,
                    message,
                    visible,
                    visibleFromState,
                    disabled,
                    disabledFromState,
                    hintPosition,
                } = this.props

                const currentVisible = !isNil(visible)
                    ? visible
                    : visibleFromState

                const currentDisabled = !isNil(disabled)
                    ? disabled
                    : disabledFromState

                const currentMessage = currentDisabled ? message || hint : hint

                const { confirm: confirmState, url, id } = this.state
                const confirm = confirmState ? {
                    ...confirmState,
                    onDeny: this.onDeny,
                    onConfirm: this.onConfirm,
                    target: id,
                } : null

                return (
                    <ActionButton
                        Component={WrappedComponent}
                        componentProps={
                            { ...omit(this.props, [
                                'isInit',
                                'targetTooltip',
                                'initialProps',
                                'registerButton',
                                'uid',
                            ]) }}
                        url={url}
                        componentDisabled={currentDisabled}
                        componentVisible={currentVisible}
                        onClick={this.onClickHandler}
                        id={id}
                        confirm={confirm}
                        hint={currentMessage}
                        placement={hintPosition}
                    />
                )
            }
        }

        const mapStateToProps = (state, {
            entityKey: containerId,
            id: buttonId,
            model: prefix,
            datasource,
            hint,
            label,
        }) => {
            const model = getModelByPrefixAndNameSelector(prefix, datasource)(state)

            return ({
                isInit: isInitSelector(state, containerId, buttonId),
                visibleFromState: isVisibleSelector(state, containerId, buttonId),
                disabledFromState: isDisabledSelector(state, containerId, buttonId),
                message: messageSelector(state, containerId, buttonId),
                count: countSelector(state, containerId, buttonId),
                toolbar: toolbarSelector(state),
                ...propsResolver({ hint, label }, model),
            })
        }

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
            visible: PropTypes.bool,
            disabled: PropTypes.bool,
            initialProps: PropTypes.object,
            hint: PropTypes.string,
            uid: PropTypes.string,
            entityKey: PropTypes.string,
            id: PropTypes.string,
            validate: PropTypes.arrayOf(PropTypes.string),
            confirm: PropTypes.object,
            registerButton: PropTypes.func,
            removeButton: PropTypes.func,
            dispatch: PropTypes.func,
            message: PropTypes.oneOf(['string', null, undefined]),
            visibleFromState: PropTypes.bool,
            disabledFromState: PropTypes.bool,
            hintPosition: PropTypes.oneOf(['left', 'top', 'right', 'bottom']),
            url: PropTypes.string,
        }

        ButtonContainer.contextType = ReactReduxContext

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
