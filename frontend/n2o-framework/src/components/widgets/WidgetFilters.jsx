import React from 'react'
import PropTypes from 'prop-types'
import { connect, ReactReduxContext } from 'react-redux'
import { getFormValues, reset } from 'redux-form'
import isEqual from 'lodash/isEqual'
import difference from 'lodash/difference'
import map from 'lodash/map'
import unset from 'lodash/unset'
import debounce from 'lodash/debounce'
import { createStructuredSelector } from 'reselect'

import { Filter } from '../snippets/Filter/Filter'
import { makeWidgetFilterVisibilitySelector } from '../../ducks/widgets/selectors'
import { validateField } from '../../core/validation/createValidator'
import propsResolver from '../../utils/propsResolver'
import { FILTER_DELAY } from '../../constants/time'

import { flatFields, getFieldsKeys } from './Form/utils'
import ReduxForm from './Form/ReduxForm'

function generateFormName(widgetId) {
    return `${widgetId}_filter`
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
 * @reactProps {function} reduxFormFilter
 */
class WidgetFilters extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            defaultValues: props.filterModel,
        }
        this.formName = generateFormName(props.widgetId)
        this.handleFilter = this.handleFilter.bind(this)
        this.handleReset = this.handleReset.bind(this)
        this.validateAndFetch = this.validateAndFetch.bind(this)
        this.debouncedHandleFilter = debounce(this.handleFilter, FILTER_DELAY)
    }

    getChildContext() {
        return {
            _widgetFilter: {
                formName: this.formName,
                filter: this.handleFilter,
                reset: this.handleReset,
            },
        }
    }

    componentDidUpdate(prevProps) {
        const { filterModel, reduxFormFilter, setFilter, validation, searchOnChange, dispatch } = this.props
        const { defaultValues } = this.state

        if (isEqual(reduxFormFilter, filterModel)) { return }

        if (
            !isEqual(prevProps.filterModel, filterModel) &&
            !isEqual(filterModel, defaultValues)
        ) {
            this.setState({
                defaultValues: filterModel,
            })
        } else if (!isEqual(reduxFormFilter, prevProps.reduxFormFilter)) {
            const { store } = this.context
            const state = store.getState()

            setFilter(reduxFormFilter)
            validateField(validation, this.formName, state, true)(reduxFormFilter, dispatch)
            if (searchOnChange) {
                this.debouncedHandleFilter()
            }
        }
    }

    handleFilter() {
        this.validateAndFetch()
    }

    handleReset() {
        const {
            fieldsets,
            blackResetList,
            reduxFormFilter,
            resetFilterModel,
            setFilter,
        } = this.props
        const newReduxForm = { ...reduxFormFilter }
        const toReset = difference(
            map(flatFields(fieldsets, []), 'id'),
            blackResetList,
        )

        toReset.forEach((field) => {
            unset(newReduxForm, field)
        })
        this.setState(
            {
                defaultValues: newReduxForm,
            },
            () => {
                resetFilterModel(this.formName)
                setFilter(newReduxForm)
                this.validateAndFetch(newReduxForm)
            },
        )
    }

    validateAndFetch(newValues) {
        const { validation, dispatch, reduxFormFilter, fetchData } = this.props
        const { store } = this.context
        const state = store.getState()
        const values = newValues || reduxFormFilter

        validateField(validation, this.formName, state, true)(
            values,
            dispatch,
        ).then((hasError) => {
            if (!hasError) {
                fetchData({ page: 1 })
            }
        })
    }

    static getDerivedStateFromProps(props, state) {
        const { fieldsets } = props
        const resolved = Object.values(propsResolver(fieldsets) || {})

        if (isEqual(resolved, state.fieldsets)) {
            return null
        }

        const fields = getFieldsKeys(resolved)

        return {
            fieldsets: resolved,
            fields,
        }
    }

    render() {
        const {
            visible,
            hideButtons,
            validation,
            filterModel,
        } = this.props
        const { defaultValues, fieldsets, fields } = this.state

        return (
            <Filter
                style={{ display: !visible ? 'none' : '' }}
                hideButtons={hideButtons}
                onSearch={this.handleFilter}
                onReset={this.handleReset}
            >
                <ReduxForm
                    form={this.formName}
                    fieldsets={fieldsets}
                    fields={fields}
                    activeModel={filterModel}
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
    reduxFormFilter: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    setFilter: PropTypes.func,
    fetchData: PropTypes.func,
    hideButtons: PropTypes.bool,
    searchOnChange: PropTypes.bool,
    dispatch: PropTypes.func,
    resetFilterModel: PropTypes.func,
}

WidgetFilters.defaultProps = {
    hideButtons: false,
    searchOnChange: false,
}

WidgetFilters.contextType = ReactReduxContext

WidgetFilters.childContextTypes = {
    _widgetFilter: PropTypes.object.isRequired,
}

const mapStateToProps = createStructuredSelector({
    visible: (state, props) => makeWidgetFilterVisibilitySelector(props.widgetId)(state, props),
    reduxFormFilter: (state, props) => getFormValues(generateFormName(props.widgetId))(state) || {},
})

const mapDispatchToProps = dispatch => ({
    dispatch,
    resetFilterModel: formName => dispatch(reset(formName)),
})

export default connect(mapStateToProps, mapDispatchToProps)(WidgetFilters)
