import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import {
    compose,
    pure,
    withProps,
    defaultProps,
    withHandlers,
    shouldUpdate,
    mapProps,
    branch,
} from 'recompose'
import { getFormValues } from 'redux-form'
import isBoolean from 'lodash/isBoolean'
import memoize from 'lodash/memoize'
import get from 'lodash/get'
import isEqual from 'lodash/isEqual'
import map from 'lodash/map'
import isNil from 'lodash/isNil'

import {
    isInitSelector,
    isVisibleSelector,
    isDisabledSelector,
    messageSelector,
    requiredSelector,
} from '../../../../ducks/form/selectors'
import { registerFieldExtra } from '../../../../ducks/form/store'
import propsResolver from '../../../../utils/propsResolver'

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
            this.onChange = this.onChange.bind(this)
            this.onFocus = this.onFocus.bind(this)
            this.onBlur = this.onBlur.bind(this)
            this.initIfNeeded()
        }

        /**
         * Регистрация дополнительных свойств поля
         */
        initIfNeeded() {
            const {
                meta: { form },
                input: { name },
                isInit,
                visibleToRegister,
                disabledToRegister,
                dependency,
                requiredToRegister,
                registerFieldExtra,
                parentIndex,
                validation,
                enabled,
            } = this.props

            if (!isInit) {
                registerFieldExtra(form, name, {
                    visible: visibleToRegister,
                    visible_field: visibleToRegister,
                    disabled: disabledToRegister && enabled === false,
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

        /**
         * мэппинг onChange
         * @param e
         */
        onChange(e) {
            const { id, input, onChange } = this.props

            if (input) {
                input.onChange(e)
            }
            if (onChange) {
                onChange(e, id)
            }
        }

        /**
         * мэппинг onBlur
         * @param e
         */
        onBlur(e) {
            const { id, input, onBlur } = this.props

            if (input) {
                input.onBlur(e)
            }

            if (onBlur) {
                onBlur(e, id)
            }
        }

        /**
         * мэппинг onFocus
         * @param e
         */
        onFocus(e) {
            const { input, onFocus } = this.props

            if (input) {
                input.onFocus(e)
            }

            if (onFocus) {
                onFocus(e.target.value)
            }
        }

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

        /**
         * мэппинг сообщений
         * @returns {string}
         */
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
                    onChange={this.onChange}
                    onBlur={this.onBlur}
                />
            )
        }
    }

    FieldContainer.propTypes = {
        id: PropTypes.string,
        mapProps: PropTypes.func,
        input: PropTypes.object,
        onChange: PropTypes.func,
        onBlur: PropTypes.func,
        onFocus: PropTypes.func,
        meta: PropTypes.object,
        isInit: PropTypes.bool,
        visibleToRegister: PropTypes.any,
        disabledToRegister: PropTypes.any,
        dependency: PropTypes.object,
        requiredToRegister: PropTypes.any,
        registerFieldExtra,
        parentIndex: PropTypes.number,
        validation: PropTypes.any,
        control: PropTypes.object,
        action: PropTypes.object,
        enabled: PropTypes.bool,
    }

    const mapStateToProps = (state, { modelPrefix, visible: propsVisible, ...ownProps }) => {
        const { form } = ownProps.meta
        const { name } = ownProps.input
        const { multiSetDisabled } = ownProps

        const visibleFromRedux = isVisibleSelector(form, name)(state)
        const visible = visibleFromRedux === undefined ? propsVisible : visibleFromRedux

        const disabled = multiSetDisabled
            ? multiSetDisabled || isDisabledSelector(form, name)(state)
            : isDisabledSelector(form, name)(state)

        return {
            isInit: isInitSelector(form, name)(state),
            visible,
            disabled,
            message: messageSelector(form, name, modelPrefix)(state),
            required: requiredSelector(form, name)(state),
            model: getFormValues(form)(state),
        }
    }

    const mapDispatchToProps = dispatch => ({
        dispatch,
        registerFieldExtra: (form, name, initialState) => dispatch(registerFieldExtra(form, name, initialState)),
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
            disabledToRegister:
                isBoolean(props.enabled) && !props.disabled
                    ? !props.enabled
                    : props.disabled,
            requiredToRegister: props.required,
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
        shouldUpdate(
            (props, nextProps) => !isEqual(props.model, nextProps.model) ||
                props.isInit !== nextProps.isInit ||
                props.visible !== nextProps.visible ||
                props.disabled !== nextProps.disabled ||
                props.message !== nextProps.message ||
                props.required !== nextProps.required ||
                props.loading !== nextProps.loading ||
                props.meta.touched !== nextProps.meta.touched ||
                props.active !== nextProps.active ||
                get(props, 'input.value', null) !== get(nextProps, 'input.value', null),
        ),
        withProps(props => ({
            ref: props.setReRenderRef,
            disabled: props.disabled,
        })),
        pure,
    )(FieldContainer)
}
