import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { getFormValues, reset } from 'redux-form'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
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

import { flatFields } from './Form/utils'
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
            searchOnChange,
            dispatch,
            validation,
        } = this.props
        const { store } = this.context
        const state = store.getState()

        validateField(validation, this.formName, state, true)(values, dispatch)
        if (searchOnChange) {
            this.debouncedHandleFilter()
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
                this.validateAndFetch(newReduxForm)
            },
        )
    }

    validateAndFetch(newValues) {
        const { setFilter, validation, dispatch, reduxFormFilter } = this.props
        const { store } = this.context
        const state = store.getState()
        const values = newValues || reduxFormFilter

        validateField(validation, this.formName, state, true)(
            values,
            dispatch,
        ).then((hasError) => {
            if (!hasError) {
                setFilter(values)
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
    reduxFormFilter: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    setFilter: PropTypes.func,
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
    visible: (state, props) => makeWidgetFilterVisibilitySelector(props.widgetId)(state, props),
    reduxFormFilter: (state, props) => getFormValues(generateFormName(props.widgetId))(state) || {},
})

const mapDispatchToProps = dispatch => ({
    dispatch,
    resetFilterModel: formName => dispatch(reset(formName)),
})

export default connect(mapStateToProps, mapDispatchToProps)(WidgetFilters)
