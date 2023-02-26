import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import cloneDeep from 'lodash/cloneDeep'
import { getFormValues, destroy } from 'redux-form'
import { createStructuredSelector } from 'reselect'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import React from 'react'

import { ModelPrefix } from '../../../core/datasource/const'
import { makeFormsByDatasourceSelector } from '../../../ducks/form/selectors'
import { syncValues } from '../../../ducks/form/store'

import { getFieldsKeys } from './utils'
import ReduxForm from './ReduxForm'

export const mapStateToProps = createStructuredSelector({
    reduxFormValues: (state, { datasource, id }) => getFormValues(datasource || id)(state) || {},
    formsByDatasource: (state, { datasource }) => makeFormsByDatasourceSelector(datasource)(state) || [],
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

    componentWillUnmount() {
        const { dispatch, datasource: datasourceId, id, formsByDatasource } = this.props

        if (formsByDatasource.length) {
            dispatch(destroy(datasourceId || id))
        }
    }

    componentDidUpdate({ models: prevModels, reduxFormValues: prevValues }) {
        const { models, reduxFormValues, form, setResolve, dispatch, id, datasource: datasourceId } = this.props
        const { datasource } = models
        const { modelPrefix } = form
        const activeModel = this.getActiveModel(models)
        const prevModel = this.getActiveModel(prevModels)
        const needUpdateValuesIfSameDatasource = datasource !== prevModels.datasource &&
            isEqual(prevModels.datasource, datasource) &&
            !isEqual(datasource[0], reduxFormValues)

        if (!isEqual(datasource, prevModels.datasource) || needUpdateValuesIfSameDatasource) {
            // если предыдущий список пустой, и есть активная модель, то это defaultValues и надо их мержить
            const model = isEmpty(prevModels.datasource) && activeModel
                ? { ...activeModel, ...datasource[0] }
                : (datasource[0] || {})

            this.setState(() => ({ initialValues: cloneDeep(model) }), () => {
                this.updateActiveModel(model)
                // фикс, чтобы отрабатывала master-detail зависимость при ините edit формы
                if (modelPrefix === ModelPrefix.edit) {
                    setResolve(model)
                }
            })
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
            dispatch(syncValues(datasourceId || id, activeModel))
            // this.setState({ initialValues: cloneDeep(activeModel) })
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

        return modelPrefix === ModelPrefix.edit ? edit : resolve
    }

    updateActiveModel(model) {
        const { setResolve, setEdit, form } = this.props
        const { modelPrefix } = form
        const updateModel = modelPrefix === ModelPrefix.edit ? setEdit : setResolve

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
        modelPrefix: PropTypes.oneOf([ModelPrefix.active, ModelPrefix.edit]),
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
    formsByDatasource: PropTypes.array,
}

export default connect(mapStateToProps)(Container)
