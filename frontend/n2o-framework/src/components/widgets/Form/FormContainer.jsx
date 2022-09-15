import {
    compose,
    getContext,
} from 'recompose'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import cloneDeep from 'lodash/cloneDeep'
import { getFormValues, initialize } from 'redux-form'
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
    reduxFormValues: (state, { modelId, widgetId }) => getFormValues(modelId || widgetId)(state) || {},
})

const fakeInitial = {}

class Container extends React.Component {
    constructor(props) {
        super(props)
        const { activeModel, datasource } = props
        const initialValues = cloneDeep(isEmpty(activeModel) ? datasource : activeModel)
        const { widgetId, fieldsets, validation, store } = this.props
        const fields = getFieldsKeys(fieldsets)

        this.state = {
            initialValues,
            validators: createValidator(validation, widgetId, store, fields),
            fields,
        }
    }

    componentDidUpdate({
        datasource: prevDatasource,
        reduxFormValues: prevValues,
        activeModel: prevModel,
    }) {
        const {
            reduxFormValues,
            dispatch,
            widgetId,
            datasource,
            activeModel,
            modelId,
            modelPrefix,
            onSetModel,
        } = this.props
        const { initialValues } = this.state

        if (!isEqual(datasource, prevDatasource)) {
            // если предыдущий список пустой, и есть активная модель, то это defaultValues и надо их мержить
            const model = isEmpty(prevDatasource) && activeModel
                ? { ...activeModel, ...datasource }
                : datasource

            if (model) {
                this.setState(() => ({ initialValues: cloneDeep(model) }), () => {
                    onSetModel(modelPrefix, null, model)
                    // фикс, чтобы отрабатывала master-detail зависимость при ините edit формы
                    if (modelPrefix === PREFIXES.edit) {
                        onSetModel(PREFIXES.resolve, null, model)
                    }
                })
            }
        } else if (
            !isEqual(reduxFormValues, prevValues) &&
            !isEqual(reduxFormValues, activeModel)
        ) {
            // Поменялись данные в форме (onChange) - актуализируем активную модель
            onSetModel(modelPrefix, null, {
                ...(activeModel || {}),
                ...reduxFormValues,
            })
        } else if (!isEqual(activeModel, prevModel) && !isEqual(activeModel, reduxFormValues)) {
            // поменялась активная модель и она отличается от того что в форме (copyActyon / setValue-dependency) - обновляем данные в форме
            // костыль для того чтобы редакс-форма подхватила новую модель, даже если она совпадает с предыдущим initialValues
            this.setState({ initialValues: fakeInitial })
        } else if (initialValues === fakeInitial) {
            this.setState({ initialValues: cloneDeep(activeModel) })
            dispatch(initialize(modelId || widgetId, initialValues))
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
        const { modelId, widgetId } = this.props

        return (
            <ReduxForm
                form={modelId || widgetId}
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
    modelId: PropTypes.string,
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
    dispatch: PropTypes.func,
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
