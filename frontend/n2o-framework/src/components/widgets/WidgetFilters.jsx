import React from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { destroy, getFormValues, reset } from 'redux-form'
import isEqual from 'lodash/isEqual'
import difference from 'lodash/difference'
import map from 'lodash/map'
import unset from 'lodash/unset'
import cloneDeep from 'lodash/cloneDeep'
import debounce from 'lodash/debounce'
import { createStructuredSelector } from 'reselect'

import { Filter } from '../snippets/Filter/Filter'
import { makeWidgetFilterVisibilitySelector } from '../../ducks/widgets/selectors'
import { generateFormFilterId } from '../../utils/generateFormFilterId'
import { FILTER_DELAY } from '../../constants/time'
import { ModelPrefix } from '../../core/datasource/const'
import { reset as resetModels } from '../../ducks/datasource/store'

import { flatFields, getFieldsKeys } from './Form/utils'
import ReduxForm from './Form/ReduxForm'
import { WidgetFiltersContext } from './WidgetFiltersContext'

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
        this.formName = generateFormFilterId(props.datasource)
        this.handleFilter = this.handleFilter.bind(this)
        this.handleReset = this.handleReset.bind(this)
        this.debouncedHandleFilter = debounce(this.handleFilter, FILTER_DELAY)

        this.contextValue = {
            formName: this.formName,
            filter: this.handleFilter.bind(this),
            reset: this.handleReset.bind(this),
        }
    }

    componentDidUpdate(prevProps) {
        const { filterModel, reduxFormFilter, setFilter, searchOnChange } = this.props
        const { defaultValues } = this.state

        if (isEqual(reduxFormFilter, filterModel)) { return }

        if (
            !isEqual(prevProps.filterModel, filterModel) &&
            !isEqual(filterModel, defaultValues)
        ) {
            this.setState({
                defaultValues: filterModel,
            })
            if (searchOnChange) {
                this.handleFilter()
            }
        } else if (!isEqual(reduxFormFilter, prevProps.reduxFormFilter)) {
            setFilter(reduxFormFilter)
            if (searchOnChange) {
                this.debouncedHandleFilter()
            }
        }
    }

    componentWillUnmount() {
        const { dispatch, datasource } = this.props

        dispatch(destroy(datasource))
    }

    handleFilter() {
        const { fetchData } = this.props

        fetchData({ page: 1 })
    }

    handleReset(fetchOnClear = true) {
        const {
            filterFieldsets,
            blackResetList,
            reduxFormFilter,
            resetFilterModel,
            resetModels,
            setFilter,
        } = this.props
        const newReduxForm = cloneDeep(reduxFormFilter)
        const toReset = difference(
            map(flatFields(filterFieldsets, []), 'id'),
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

        this.setState({ defaultValues: fakeDefaultValues },
            () => this.setState(
                {
                    defaultValues: newReduxForm,
                },
                () => {
                    resetFilterModel(this.formName)
                    setFilter(newReduxForm)

                    if (fetchOnClear) {
                        this.handleFilter()
                    } else {
                        resetModels(this.formName)
                    }
                },
            ))
    }

    static getDerivedStateFromProps(props, state) {
        const { filterFieldsets } = props

        if (isEqual(filterFieldsets, state.fieldsets)) {
            return null
        }

        const fields = getFieldsKeys(filterFieldsets)

        return {
            fieldsets: filterFieldsets,
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
            <WidgetFiltersContext.Provider value={this.contextValue}>
                <Filter
                    visible={visible}
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
                        modelPrefix={ModelPrefix.filter}
                    />
                </Filter>
            </WidgetFiltersContext.Provider>
        )
    }
}

WidgetFilters.propTypes = {
    datasource: PropTypes.string,
    filterFieldsets: PropTypes.array,
    visible: PropTypes.bool,
    blackResetList: PropTypes.array,
    filterModel: PropTypes.object,
    validation: PropTypes.object,
    reduxFormFilter: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
    setFilter: PropTypes.func,
    fetchData: PropTypes.func,
    hideButtons: PropTypes.bool,
    searchOnChange: PropTypes.bool,
    resetFilterModel: PropTypes.func,
    resetModels: PropTypes.func,
    dispatch: PropTypes.func,
}

WidgetFilters.defaultProps = {
    hideButtons: false,
    searchOnChange: false,
}

const mapStateToProps = createStructuredSelector({
    visible: (state, props) => makeWidgetFilterVisibilitySelector(props.widgetId)(state, props),
    reduxFormFilter: (state, props) => getFormValues(generateFormFilterId(props.datasource))(state) || {},
})

const mapDispatchToProps = dispatch => ({
    dispatch,
    resetFilterModel: formName => dispatch(reset(formName)),
    resetModels: formName => dispatch(resetModels(formName)),
})

export default connect(mapStateToProps, mapDispatchToProps)(WidgetFilters)
