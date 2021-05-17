import {
    compose,
    withHandlers,
    withProps,
    onlyUpdateForKeys,
    withPropsOnChange,
    getContext,
} from 'recompose'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import merge from 'deepmerge'
import { getFormValues } from 'redux-form'
import { createStructuredSelector } from 'reselect'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'

import widgetContainer from '../WidgetContainer'
import { FORM } from '../widgetTypes'
import createValidator from '../../../core/validation/createValidator'
import { PREFIXES } from '../../../constants/models'

import { getFieldsKeys } from './utils'
import ReduxForm from './ReduxForm'

export const withWidgetContainer = widgetContainer(
    {
        mapProps: props => ({
            widgetId: props.widgetId,
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
    reduxFormValues: (state, props) => getFormValues(props.form)(state) || {},
})

const mergeInitial = (model, datasource) => merge(
    model || {},
    datasource || {},
    {
        arrayMerge: (destinationArray, sourceArray) => sourceArray,
    },
)

/**
 * Изменение initialValues, если поменялась модель
 */
export const onModelChange = withPropsOnChange(
    (props, nextProps) => (
        !isEqual(props.resolveModel || {}, nextProps.resolveModel) &&
        !isEqual(nextProps.activeModel, nextProps.reduxFormValues) &&
        isEqual(props.reduxFormValues, nextProps.reduxFormValues)
    ),
    props => ({
        initialValues: isEmpty(props.activeModel)
            ? mergeInitial(props.resolveModel, props.datasource)
            : props.activeModel,
    }),
)

/**
 * Изменение initialValues, если поменялся datasource
 */
export const onDataSourceChange = withPropsOnChange(
    (props, nextProps) => !isEqual(props.datasource, nextProps.datasource),
    (props) => {
        const initialValues = mergeInitial(props.resolveModel, props.datasource)

        /*
         * Если передать одинаковые значения в двух withPropsOnChange, то рекомпос ломается
         * и последующие данные из onModelChange не долетают нормально :)
         */
        if (isEqual(initialValues, props.initialValues)) {
            return {}
        }

        return { initialValues }
    },
)

export const withWidgetHandlers = withHandlers({
    onChange: props => (values, dispatch, options, prevValues) => {
        if (props.setActive) {
            props.setActive()
        }
        if (
            props.modelPrefix &&
            isEqual(props.initialValues, props.reduxFormValues) &&
            !isEqual(props.initialValues, props.resolveModel)
        ) {
            props.onResolve(props.initialValues)
        }

        if ((isEmpty(values) && isEmpty(prevValues)) || !props.modelPrefix) {
            props.onResolve(values)
        } else if (!isEqual(props.reduxFormValues, prevValues)) {
            props.onSetModel(
                props.modelPrefix || PREFIXES.resolve,
                props.widgetId,
                props.reduxFormValues,
            )
        }
    },
})

/**
 * Обертка в widgetContainer, мэппинг пропсов
 */
export default compose(
    withWidgetContainer,
    getContext({
        store: PropTypes.object,
    }),
    withProps((props) => {
        const fields = getFieldsKeys(props.fieldsets)

        return {
            form: props.widgetId,
            prompt: props.prompt,
            store: props.store,
            fields,
            ...createValidator(props.validation, props.widgetId, props.store, fields),
            ...props,
        }
    }),
    connect(mapStateToProps),
    onModelChange,
    onDataSourceChange,
    withWidgetHandlers,
    onlyUpdateForKeys(['initialValues', 'fields']),
)(ReduxForm)
