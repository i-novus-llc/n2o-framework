import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import {
    compose,
    pure,
    withProps,
    defaultProps,
    withHandlers,
    mapProps,
    branch,
} from 'recompose'
import isBoolean from 'lodash/isBoolean'
import memoize from 'lodash/memoize'
import get from 'lodash/get'
import map from 'lodash/map'
import isNil from 'lodash/isNil'

import {
    isDisabledSelector,
    isInitSelector, isVisibleSelector,
    messageSelector, requiredSelector,
} from '../../../../ducks/form/selectors'
import { registerFieldExtra, unRegisterExtraField } from '../../../../ducks/form/store'
import propsResolver from '../../../../utils/propsResolver'
import { getModelByPrefixAndNameSelector } from '../../../../ducks/models/selectors'

import withAutoSave from './withAutoSave'

const INDEX_PLACEHOLDER = 'index'

/**
 * HOC обертка для полей, в которой содержится мэппинг свойств редакса и регистрация дополнительных свойств полей
 * @param Field
 * @returns {*}
 */
export default (Field) => {
    class FieldContainer extends React.Component {
        constructor(props) {
            super(props)
            this.initIfNeeded(props)
        }

        componentWillUnmount() {
            const { unRegisterExtraField, form, name, modelPrefix } = this.props

            unRegisterExtraField(modelPrefix, form, name)
        }

        /**
         * Регистрация дополнительных свойств поля
         */
        initIfNeeded(props) {
            const {
                form,
                name,
                isInit,
                visibleToRegister,
                disabledToRegister,
                dependency,
                requiredToRegister,
                registerFieldExtra,
                parentIndex,
                validation,
                modelPrefix,
            } = props

            if (!isInit) {
                registerFieldExtra(modelPrefix, form, name, {
                    visible: visibleToRegister,
                    visible_field: visibleToRegister,
                    disabled: disabledToRegister,
                    disabled_field: disabledToRegister,
                    parentIndex,
                    dependency: this.modifyDependency(dependency, parentIndex),
                    required: requiredToRegister,
                    validation,
                })
            }
        }

        modifyDependency = (dependency, parentIndex) => {
            if (!isNil(parentIndex)) {
                return map(dependency, (dep) => {
                    const newDep = { ...dep }

                    if (newDep.on) {
                        newDep.on = this.modifyOn(newDep.on, parentIndex)
                    }

                    return newDep
                })
            }

            return dependency
        }

        modifyOn = (on, parentIndex) => on.map(key => key.replace(INDEX_PLACEHOLDER, parentIndex))

        replaceIndex = (obj, index) => JSON.parse(
            JSON.stringify(obj).replaceAll(INDEX_PLACEHOLDER, index),
        )

        resolveControlIndexes = (control) => {
            const { parentIndex } = this.props

            if (control && control.dataProvider && !isNil(parentIndex)) {
                const dataProvider = this.replaceIndex(control.dataProvider, parentIndex)

                return {
                    ...control,
                    dataProvider,
                }
            }

            return control
        }

        resolveActionIndexes = (action) => {
            const { parentIndex } = this.props

            if (action && !isNil(parentIndex)) {
                return this.replaceIndex(action, parentIndex)
            }

            return action
        }

        render() {
            const { mapProps } = this.props
            const mappedProps = mapProps(this.props)
            const { control, action, subMenu } = mappedProps

            if (subMenu) {
                mappedProps.subMenu = subMenu.map((option) => {
                    const { action } = option

                    if (action) {
                        option.action = this.resolveActionIndexes(action)
                    }

                    return option
                })
            }

            return (
                <Field
                    {...mappedProps}
                    control={this.resolveControlIndexes(control)}
                    action={this.resolveActionIndexes(action)}
                />
            )
        }
    }

    FieldContainer.propTypes = {
        id: PropTypes.string,
        mapProps: PropTypes.func,
        input: PropTypes.object,
        parentIndex: PropTypes.number,
    }

    const mapStateToProps = (state, ownProps) => {
        const { form: formName, name: fieldName, modelPrefix } = ownProps

        return {
            isInit: isInitSelector(modelPrefix, formName, fieldName)(state),
            visible: isVisibleSelector(modelPrefix, formName, fieldName)(state),
            disabled: isDisabledSelector(modelPrefix, formName, fieldName)(state),
            required: requiredSelector(modelPrefix, formName, fieldName)(state),
            message: messageSelector(formName, fieldName, modelPrefix)(state),
            model: getModelByPrefixAndNameSelector(modelPrefix, formName)(state),
        }
    }

    const mapDispatchToProps = dispatch => ({
        dispatch,
        registerFieldExtra: (prefix, form, name, initialState) => (
            dispatch(registerFieldExtra(prefix, form, name, initialState))
        ),
        unRegisterExtraField: (prefix, form, name) => dispatch(unRegisterExtraField(prefix, form, name)),
    })

    return compose(
        defaultProps({
            disabled: false,
            required: false,
        }),
        withHandlers({
            getValidationState: () => (message) => {
                if (!message) {
                    return false
                }

                if (message.severity === 'success') {
                    return 'is-valid'
                }

                if (message.severity === 'warning') {
                    return 'has-warning'
                }

                return 'is-invalid'
            },
        }),
        withHandlers({
            mapProps: ({ getValidationState }) => memoize((props) => {
                if (!props) {
                    return false
                }

                const { input, message, meta, model, html, content, ...rest } = props
                const pr = propsResolver(rest, model, ['toolbar'])

                return {
                    ...pr,
                    ...meta,
                    validationClass: getValidationState(message),
                    message,
                    html,
                    content,
                    model,
                    ...input,
                }
            }),
        }),
        withProps(props => ({
            visibleToRegister: props.visible,
            requiredToRegister: props.required,
            disabledToRegister:
                isBoolean(props.enabled) && !props.disabled
                    ? !props.enabled
                    : props.disabled,
        })),
        connect(
            mapStateToProps,
            mapDispatchToProps,
        ),
        mapProps(({ model, parentIndex, parentName, ...props }) => ({
            ...props,
            parentIndex,
            model: !isNil(parentName)
                ? {
                    ...get(model, parentName),
                    index: parentIndex,
                    ...model,
                }
                : model,
        })),
        branch(
            ({ dataProvider, autoSubmit }) => !!autoSubmit || !!dataProvider,
            withAutoSave,
        ),
        withProps(props => ({
            ref: props.setReRenderRef,
            disabled: props.disabled,
        })),
        pure,
    )(FieldContainer)
}
