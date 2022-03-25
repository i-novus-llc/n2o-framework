import {
    compose,
    getContext,
} from 'recompose'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import merge from 'deepmerge'
import { getFormValues } from 'redux-form'
import { createStructuredSelector } from 'reselect'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import React from 'react'

import widgetContainer from '../WidgetContainer'
import { FORM } from '../widgetTypes'
import createValidator from '../../../core/validation/createValidator'
import { PREFIXES } from '../../../ducks/models/constants'

import { getFieldsKeys } from './utils'
import ReduxForm from './ReduxForm'

export const withWidgetContainer = widgetContainer(
    {
        mapProps: props => ({
            widgetId: props.widgetId,
            modelId: props.modelId,
            isEnabled: props.isEnabled,
            pageId: props.pageId,
            autoFocus: props.autoFocus,
            fieldsets: props.fieldsets,
            datasource: props.datasource && props.datasource[0],
            onSetModel: props.onSetModel,
            onResolve: props.onResolve,
            resolveModel: props.resolveModel,
            activeModel: props.activeModel,
            validation: props.validation,
            modelPrefix: props.modelPrefix,
            prompt: props.prompt,
            setActive: props.onFocus,
            placeholder: props.placeholder,
            autoSubmit: props.autoSubmit,
        }),
    },
    FORM,
)

export const mapStateToProps = createStructuredSelector({
    reduxFormValues: (state, props) => getFormValues(props.widgetId)(state) || {},
})

const additionalMerge = (target, source) => merge(
    target || {},
    source || {},
    {
        arrayMerge: (destinationArray, sourceArray) => sourceArray,
    },
)

const mergeInitial = (target, source) => additionalMerge(additionalMerge({}, target), source)

class Container extends React.Component {
    constructor(props) {
        super(props)
        const { resolveModel, datasource } = props
        const initialValues = mergeInitial(resolveModel, datasource)
        const { widgetId, fieldsets, validation, store } = this.props
        const fields = getFieldsKeys(fieldsets)

        this.state = {
            initialValues,
            validators: createValidator(validation, widgetId, store, fields),
            fields,
        }
    }

    componentDidUpdate(prevProps) {
        const { datasource, resolveModel, reduxFormValues, activeModel } = this.props
        const { initialValues } = this.state
        let initial
        let changed = false

        if (!isEqual(datasource, prevProps.datasource)) {
            initial = mergeInitial(resolveModel, datasource)
            changed = true
        }
        if (
            !isEqual(resolveModel || {}, prevProps.resolveModel) &&
            !isEqual(activeModel, reduxFormValues) &&
            isEqual(reduxFormValues, prevProps.reduxFormValues)
        ) {
            initial = mergeInitial(datasource, isEmpty(activeModel) ? resolveModel : activeModel)
            changed = true
        }
        if (changed && !isEqual(initialValues, initial)) {
            this.setState({ initialValues: initial })
        }
    }

    onChange = (values, dispatch, options, prevValues) => {
        const {
            setActive, modelPrefix, reduxFormValues,
            resolveModel, onResolve, onSetModel, widgetId,
        } = this.props

        if (isEmpty(reduxFormValues)) {
            return
        }

        const { initialValues, validators } = this.state

        if (setActive) {
            setActive()
        }
        if (
            modelPrefix &&
            isEqual(initialValues, reduxFormValues) &&
            !isEqual(initialValues, resolveModel)
        ) {
            onResolve(initialValues)
        }

        if ((isEmpty(values) && isEmpty(prevValues)) || !modelPrefix) {
            onResolve(values)
        } else if (!isEqual(reduxFormValues, prevValues)) {
            onSetModel(
                modelPrefix || PREFIXES.resolve,
                widgetId,
                reduxFormValues,
            )
        }

        /*
         * редакс-форма не вызывает валидацию, если change не был вызван пользователем,
         * а пришёл откуда-то асинхронно (из саг), даже если диспачить startAsyncValidation & etc.
         *
         * поэтому пока, для фикса, отказываемя от asyncChangeFields и вызываем валидацию сами по изменениям
         * TODO разобраться как обойти или отказаться от редакс-формы в пользу чего-то другого
         */
        validators.asyncValidate(reduxFormValues, dispatch)
    }

    render() {
        const { initialValues, validators, fields } = this.state
        const { widgetId } = this.props

        return (
            <ReduxForm
                form={widgetId}
                fields={fields}
                {...validators}
                {...this.props}
                initialValues={initialValues}
                onChange={this.onChange}
            />
        )
    }
}

Container.propTypes = {
    form: PropTypes.string,
    modelPrefix: PropTypes.string,
    widgetId: PropTypes.string.isRequired,
    setActive: PropTypes.func,
    onResolve: PropTypes.func,
    onSetModel: PropTypes.func,
    resolveModel: PropTypes.object,
    reduxFormValues: PropTypes.object,
    datasource: PropTypes.object,
    activeModel: PropTypes.object,
    fieldsets: PropTypes.any,
    validation: PropTypes.any,
    store: PropTypes.any,
}

/**
 * Обертка в widgetContainer, мэппинг пропсов
 */
export default compose(
    withWidgetContainer,
    getContext({
        store: PropTypes.object,
    }),
    connect(mapStateToProps),
)(Container)
