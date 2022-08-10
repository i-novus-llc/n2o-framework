import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import cloneDeep from 'lodash/cloneDeep'
import { getFormValues, initialize } from 'redux-form'
import { createStructuredSelector } from 'reselect'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import React from 'react'

import { MODEL_PREFIX } from '../../../core/datasource/const'

import { getFieldsKeys } from './utils'
import ReduxForm from './ReduxForm'

const fakeInitial = {}

export const mapStateToProps = createStructuredSelector({
    reduxFormValues: (state, { datasource, id }) => getFormValues(datasource || id)(state) || {},
})

/*
 * FIXME: Разобраться с edit модулью формы
 * a) дефолтные значения всегда почему-то приходят в resolve модели
 * б) зависимость от edit формы всегда приходит на resolve модель
 */
class Container extends React.Component {
    constructor(props) {
        super(props)
        const { models, form } = props
        const { resolve, datasource } = models
        const initialValues = cloneDeep(isEmpty(resolve) ? datasource?.[0] : resolve)
        const fields = getFieldsKeys(form.fieldsets)

        this.state = {
            initialValues,
            fields,
        }
    }

    componentDidUpdate({ models: prevModels, reduxFormValues: prevValues }) {
        const { models, reduxFormValues, form, setResolve, dispatch, id, datasource: datasourceId } = this.props
        const { initialValues } = this.state
        const { datasource } = models
        const { modelPrefix } = form
        const activeModel = this.getActiveModel(models)
        const prevModel = this.getActiveModel(prevModels)

        if (!isEqual(datasource, prevModels.datasource)) {
            // если предыдущий список пустой, и есть активная модель, то это defaultValues и надо их мержить
            const model = isEmpty(prevModels.datasource) && activeModel
                ? { ...activeModel, ...datasource[0] }
                : datasource[0]

            this.updateActiveModel(model)
            // фикс, чтобы отрабатывала master-detail зависимость при ините edit формы
            if (modelPrefix === MODEL_PREFIX.edit) {
                setResolve(model)
            }
        } else if (
            !isEqual(reduxFormValues, prevValues) &&
            !isEqual(reduxFormValues, activeModel)
        ) {
            // Поменялись данные в форме (onChange) - актуализируем активную модель
            this.updateActiveModel({
                ...(activeModel || {}),
                ...reduxFormValues,
            })
        } else if (!isEqual(activeModel, prevModel) && !isEqual(activeModel, reduxFormValues)) {
            // поменялась активная модель и она отличается от того что в форме (copyActyon / setValue-dependency) - обновляем данные в форме
            // костыль для того чтобы редакс-форма подхватила новую модель, даже если она совпадает с предыдущим initialValues
            this.setState({ initialValues: fakeInitial })
        } else if (initialValues === fakeInitial) {
            this.setState({ initialValues: cloneDeep(activeModel) })
            dispatch(initialize(datasourceId || id, initialValues))
        }
    }

    onChange = (/* values, dispatch, options, prevValues */) => {
        const {
            setActive, isActive,
        } = this.props

        if (setActive && !isActive) {
            setActive()
        }
    }

    getActiveModel(models = {}) {
        const { form } = this.props
        const { resolve, edit } = models
        const { modelPrefix } = form

        return modelPrefix === MODEL_PREFIX.edit ? edit : resolve
    }

    updateActiveModel(model) {
        const { setResolve, setEdit, form } = this.props
        const { modelPrefix } = form
        const updateModel = modelPrefix === MODEL_PREFIX.edit ? setEdit : setResolve

        return updateModel(model)
    }

    render() {
        const { initialValues, fields } = this.state
        const { id, form, models, datasource } = this.props
        const activeModel = this.getActiveModel(models)

        return (
            <ReduxForm
                form={datasource || id}
                fields={fields}
                {...form}
                activeModel={activeModel}
                initialValues={initialValues}
                onChange={this.onChange}
            />
        )
    }
}

Container.propTypes = {
    form: PropTypes.shape({
        fieldsets: PropTypes.array,
        prompt: PropTypes.bool,
        modelPrefix: PropTypes.oneOf([MODEL_PREFIX.active, MODEL_PREFIX.edit]),
    }),
    id: PropTypes.string.isRequired,
    datasource: PropTypes.string,
    setActive: PropTypes.func,
    setResolve: PropTypes.func,
    setEdit: PropTypes.func,
    dispatch: PropTypes.func,
    isActive: PropTypes.bool,
    models: PropTypes.object,
    reduxFormValues: PropTypes.object,
}

export default connect(mapStateToProps)(Container)
