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
import replace from 'lodash/replace'
import includes from 'lodash/includes'
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

import withFieldValidate from './withFieldValidate'
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
            this.initIfNeeded(props)
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
                parentIndex,
                validation,
            } = props

            if (!isInit) {
                registerFieldExtra(form, name, {
                    visible: visibleToRegister,
                    visible_field: visibleToRegister,
                    disabled: disabledToRegister,
                    disabled_field: disabledToRegister,
                    dependency: this.modifyDependency(dependency, parentIndex),
                    required: requiredToRegister,
                    validation,
                })
            }
        }

    modifyDependency = (dependency, parentIndex) => {
        if (!isNil(parentIndex)) {
            return map(dependency, (dep) => {
                const { expression, on } = dep
                let newDep = { ...dep }

                if (expression) {
                    newDep = { ...newDep, expression: replace(expression, INDEX_PLACEHOLDER, parentIndex) }
                }

                if (on) {
                    newDep = { ...newDep, on: this.modifyOn(on, parentIndex) }
                }

                return newDep
            })
        }

        return dependency
    };

    modifyOn = (on, parentIndex) => map(on, key => (includes(key, INDEX_PLACEHOLDER)
        ? replace(key, INDEX_PLACEHOLDER, parentIndex)
        : key));

    /**
     * мэппинг onChange
     * @param e
     */
    onChange(e) {
        const { input, onChange } = this.props

        if (input) {
            input.onChange(e)
        }

        if (onChange) {
            onChange(e)
        }
    }

    /**
     * мэппинг onBlur
     * @param e
     */
    onBlur(e) {
        const { input, onBlur } = this.props

        if (input) {
            input.onBlur(e)
        }

        if (onBlur) {
            onBlur(e.target.value)
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

    /**
     * мэппинг сообщений
     * @param error
     * @returns {string}
     */

    render() {
        const { mapProps } = this.props

        const props = mapProps(this.props)

        return <Field {...props} />
    }
    }

    FieldContainer.propTypes = {
        mapProps: PropTypes.func,
        input: PropTypes.object,
        onChange: PropTypes.func,
        onBlur: PropTypes.func,
        onFocus: PropTypes.func,
    }

    const mapStateToProps = (state, ownProps) => {
        const { form } = ownProps.meta
        const { name } = ownProps.input

        return {
            isInit: isInitSelector(form, name)(state),
            visible: isVisibleSelector(form, name)(state),
            disabled: isDisabledSelector(form, name)(state),
            message: messageSelector(form, name)(state),
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

                const { input, message, meta, model, ...rest } = props
                const pr = propsResolver(rest, model, ['toolbar'])

                return {
                    ...pr,
                    ...meta,
                    validationClass: getValidationState(message),
                    message,
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
        branch(({ validation }) => !!validation, withFieldValidate),
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
