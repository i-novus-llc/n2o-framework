import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { getFormValues, reset } from 'redux-form';
import { isEqual, difference, map, isEmpty, unset, debounce } from 'lodash';
import { createStructuredSelector } from 'reselect';

import ReduxForm from './Form/ReduxForm';
import Filter from '../snippets/Filter/Filter';
import { dataRequestWidget } from '../../actions/widgets';
import { PREFIXES } from '../../constants/models';
import { setModel, removeModel } from '../../actions/models';
import { flatFields } from './Form/utils';
import { makeGetFilterModelSelector } from '../../selectors/models';
import { makeWidgetFilterVisibilitySelector } from '../../selectors/widgets';
import { validateField } from '../../core/validation/createValidator';

function generateFormName(props) {
  return `${props.widgetId}_filter`;
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
    super(props);
    this.state = {
      defaultValues: props.filterModel,
    };
    this.formName = generateFormName(props);
    this.values = {};
    this.handleChangeModel = this.handleChangeModel.bind(this);
    this.handleFilter = this.handleFilter.bind(this);
    this.handleReset = this.handleReset.bind(this);
    this.validateAndFetch = this.validateAndFetch.bind(this);
    this.debouncedHandleFilter = debounce(this.handleFilter, 1000);
  }

  getChildContext() {
    return {
      _widgetFilter: {
        formName: this.formName,
        changeModel: this.handleChangeModel,
        filter: this.handleFilter,
        reset: this.handleReset,
      },
    };
  }

  componentDidUpdate(prevProps, prevState) {
    const { filterModel, reduxFormFilter } = this.props;
    const { defaultValues } = this.state;
    if (
      !isEqual(prevProps.filterModel, filterModel) &&
      !isEqual(filterModel, defaultValues) &&
      !isEqual(filterModel, reduxFormFilter)
    ) {
      this.setState(() => ({
        defaultValues: filterModel,
      }));
    }
  }

  handleChangeModel(values) {
    const {
      widgetId,
      filterModel,
      setFilterModel,
      searchOnChange,
    } = this.props;

    if (!isEqual(filterModel, values)) {
      this.values = { ...values };
      setFilterModel(widgetId, values);
      if (searchOnChange) {
        this.debouncedHandleFilter();
      }
    }
  }

  handleFilter() {
    this.validateAndFetch({ page: 1, withoutSelectedId: true });
  }

  handleReset() {
    const {
      fieldsets,
      blackResetList,
      widgetId,
      reduxFormFilter,
      setFilterModel,
      resetFilterModel,
    } = this.props;
    let newReduxForm = { ...reduxFormFilter };
    const toReset = difference(
      map(flatFields(fieldsets, []), 'id'),
      blackResetList
    );
    toReset.forEach(field => {
      unset(newReduxForm, field);
    });
    this.setState(
      {
        defaultValues: newReduxForm,
      },
      () => {
        resetFilterModel(this.formName);
        setFilterModel(widgetId, newReduxForm);
        this.validateAndFetch({ withoutSelectedId: true });
      }
    );
  }

  validateAndFetch(options) {
    const { widgetId, fetchWidget, validation, dispatch } = this.props;
    const { store } = this.context;
    validateField(validation, this.formName, store.getState(), true)(
      this.values,
      dispatch
    ).then(hasError => {
      if (!hasError) {
        fetchWidget(widgetId, options);
      }
    });
  }

  render() {
    const { fieldsets, visible, hideButtons, validation } = this.props;
    const { defaultValues } = this.state;
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
          onChange={this.handleChangeModel}
          initialValues={defaultValues}
          validation={validation}
        />
      </Filter>
    );
  }
}

WidgetFilters.propTypes = {
  widgetId: PropTypes.string,
  fieldsets: PropTypes.array,
  visible: PropTypes.bool,
  blackResetList: PropTypes.array,
  filterModel: PropTypes.object,
  validation: PropTypes.object,
  clearFilterModel: PropTypes.func,
  setFilterModel: PropTypes.func,
  reduxFormFilter: PropTypes.oneOfType([PropTypes.func, PropTypes.object]),
  fetchWidget: PropTypes.func,
  hideButtons: PropTypes.bool,
  searchOnChange: PropTypes.bool,
};

WidgetFilters.defaultProps = {
  hideButtons: false,
  searchOnChange: false,
};

WidgetFilters.contextTypes = {
  store: PropTypes.object,
};

WidgetFilters.childContextTypes = {
  _widgetFilter: PropTypes.object.isRequired,
};

const mapStateToProps = createStructuredSelector({
  filterModel: (state, props) =>
    makeGetFilterModelSelector(props.widgetId)(state, props),
  visible: (state, props) =>
    makeWidgetFilterVisibilitySelector(props.widgetId)(state, props),
  reduxFormFilter: (state, props) =>
    getFormValues(generateFormName(props))(state) || {},
});

const mapDispatchToProps = dispatch => {
  return {
    dispatch,
    setFilterModel: (widgetId, data) =>
      dispatch(setModel(PREFIXES.filter, widgetId, data)),
    fetchWidget: (widgetId, options) =>
      dispatch(dataRequestWidget(widgetId, options)),
    clearFilterModel: widgetId =>
      dispatch(removeModel(PREFIXES.filter, widgetId)),
    resetFilterModel: formName => dispatch(reset(formName)),
  };
};

WidgetFilters = connect(
  mapStateToProps,
  mapDispatchToProps
)(WidgetFilters);

export default WidgetFilters;
