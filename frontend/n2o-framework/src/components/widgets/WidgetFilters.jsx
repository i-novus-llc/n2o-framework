import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { getFormValues, reset } from 'redux-form'
import { batchActions } from 'redux-batched-actions'
import cloneDeep from 'lodash/cloneDeep'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import difference from 'lodash/difference'
import map from 'lodash/map'
import unset from 'lodash/unset'
import debounce from 'lodash/debounce'
import { createStructuredSelector } from 'reselect'

import { Filter } from '../snippets/Filter/Filter'
import { dataRequestWidget } from '../../ducks/widgets/store'
import { PREFIXES } from '../../ducks/models/constants'
import { setModel, removeModel } from '../../ducks/models/store'
import { makeGetFilterModelSelector } from '../../ducks/models/selectors'
import { makeWidgetFilterVisibilitySelector } from '../../ducks/widgets/selectors'
import { validateField } from '../../core/validation/createValidator'
import propsResolver from '../../utils/propsResolver'
import { FILTER_DELAY } from '../../constants/time'

import { flatFields } from './Form/utils'
import ReduxForm from './Form/ReduxForm'

function generateFormName(props) {
    return `${props.widgetId}_filter`
}

/**
 * Компонент WidgetFilters
 * @reactProps {string} widgetId
 * @reactProps {array} fieldsets
 * @reactProps {boolean} visible
 * @reactProps {boolean} hideButtons
 * @reactProps {array} blackResetList
 * @reactProps {object} filterModel
 * @reactProps {function} fetchWidget
 * @reactProps {function} clearFilterModel
 * @reactProps {function} setFilterModel
 * @reactProps {function} reduxFormFilter
 */
class WidgetFilters extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            defaultValues: props.filterModel,
        }
        this.formName = generateFormName(props)
        this.handleChangeModel = this.handleChangeModel.bind(this)
        this.handleFilter = this.handleFilter.bind(this)
        this.handleReset = this.handleReset.bind(this)
        this.validateAndFetch = this.validateAndFetch.bind(this)
        this.debouncedHandleFilter = debounce(this.handleFilter, FILTER_DELAY)
    }

    getChildContext() {
        return {
            _widgetFilter: {
                formName: this.formName,
                changeModel: this.handleChangeModel,
                filter: this.handleFilter,
                reset: this.handleReset,
            },
        }
    }

    componentDidUpdate(prevProps) {
        const { filterModel, reduxFormFilter } = this.props
        const { defaultValues } = this.state

        if (
            !isEqual(prevProps.filterModel, filterModel) &&
            !isEqual(filterModel, defaultValues) &&
            !isEqual(filterModel, reduxFormFilter)
        ) {
            this.setState(() => ({
                defaultValues: filterModel,
            }))
        }
    }

    handleChangeModel(values) {
        const {
            widgetId,
            setFilterModel,
            searchOnChange,
            dispatch,
            validation,
        } = this.props
        const { store } = this.context
        const state = store.getState()

        setFilterModel(widgetId, values)
        validateField(validation, this.formName, state, true)(values, dispatch)
        if (searchOnChange) {
            this.debouncedHandleFilter()
        }
    }

    handleFilter() {
        this.validateAndFetch({ page: 1 })
    }

    handleReset() {
        const {
            fieldsets,
            blackResetList,
            widgetId,
            reduxFormFilter,
            setFilterModel,
            resetFilterModel,
        } = this.props

        const newReduxForm = cloneDeep(reduxFormFilter)
        const toReset = difference(
            map(flatFields(fieldsets, []), 'id'),
            blackResetList,
        )

        toReset.forEach((field) => {
            unset(newReduxForm, field)
        })

        /*
          fakeDefaultValues HACK!
          для button выполняющего redux-form/RESET
          Если defaultValues = {} и newReduxForm = {}
          redux-form не кидает reinitialize из за того что defaultValues не поменялись,
          -> не срабатывают field dependency завязанные на actionTypes.INITIALIZE
          (прим. не меняется enabled зависимого поля)
        */

        const fakeDefaultValues = Date.now()

        this.setState({ defaultValues: fakeDefaultValues }, () => {
            this.setState(
                {
                    defaultValues: newReduxForm,
                },
                () => {
                    resetFilterModel(this.formName)
                    setFilterModel(widgetId, newReduxForm)
                    this.validateAndFetch()
                },
            )
        })
    }

    validateAndFetch(options) {
        const { widgetId, fetchWidget, validation, dispatch } = this.props
        const { store } = this.context
        const state = store.getState()
        const values = getFormValues(`${widgetId}_filter`)(state)

        validateField(validation, this.formName, state, true)(
            values,
            dispatch,
        ).then((hasError) => {
            if (!hasError) {
                fetchWidget(widgetId, options)
            }
        })
    }

    render() {
        const {
            fieldsets,
            visible,
            hideButtons,
            validation,
            filterModel,
        } = this.props
        const { defaultValues } = this.state

        return (
            <Filter
                style={{ display: !visible ? 'none' : '' }}
                hideButtons={hideButtons}
                onSearch={this.handleFilter}
                onReset={this.handleReset}
            >
                <ReduxForm
                    form={this.formName}
                    fieldsets={propsResolver(
                        fieldsets,
                        !isEmpty(filterModel) ? filterModel : defaultValues,
                    )}
                    onChange={this.handleChangeModel}
                    initialValues={defaultValues}
                    validation={validation}
                />
            </Filter>
        )
    }
}

WidgetFilters.propTypes = {
    widgetId: PropTypes.string,
    fieldsets: PropTypes.array,
    visible: PropTypes.bool,
    blackResetList: PropTypes.array,
    filterModel: PropTypes.object,
    validation: PropTypes.object,
    setFilterModel: PropTypes.func,
    reduxFormFilter: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    fetchWidget: PropTypes.func,
    hideButtons: PropTypes.bool,
    searchOnChange: PropTypes.bool,
    dispatch: PropTypes.func,
    resetFilterModel: PropTypes.func,
}

WidgetFilters.defaultProps = {
    hideButtons: false,
    searchOnChange: false,
}

WidgetFilters.contextTypes = {
    store: PropTypes.object,
}

WidgetFilters.childContextTypes = {
    _widgetFilter: PropTypes.object.isRequired,
}

const mapStateToProps = createStructuredSelector({
    filterModel: (state, props) => makeGetFilterModelSelector(props.widgetId)(state, props),
    visible: (state, props) => makeWidgetFilterVisibilitySelector(props.widgetId)(state, props),
    reduxFormFilter: (state, props) => getFormValues(generateFormName(props))(state) || {},
})

const mapDispatchToProps = (dispatch, { modelId }) => ({
    dispatch,
    setFilterModel: (widgetId, data) => dispatch(setModel(PREFIXES.filter, modelId, data)),
    fetchWidget: (widgetId, options) => dispatch(dataRequestWidget(widgetId, modelId, options)),
    clearFilterModel: () => dispatch(removeModel(PREFIXES.filter, modelId)),
    resetFilterModel: formName => dispatch(reset(formName)),
    resetFilter: formName => dispatch(
        batchActions([removeModel(PREFIXES.filter, modelId), reset(formName)]),
    ),
})

export default connect(mapStateToProps, mapDispatchToProps)(WidgetFilters)
