import React from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { getFormValues, reset } from 'redux-form';
import { isEqual, difference, map, isEmpty } from 'lodash';
import { createStructuredSelector } from 'reselect';

import ReduxForm from './Form/ReduxForm';
import Filter from '../snippets/Filter/Filter';
import { dataRequestWidget } from '../../actions/widgets';
import { PREFIXES } from '../../constants/models';
import { setModel, removeModel } from '../../actions/models';
import { flatFields } from './Form/utils';
import { makeGetFilterModelSelector } from '../../selectors/models';
import { makeWidgetFilterVisibilitySelector } from '../../selectors/widgets';

function generateFormName(props) {
  return `${props.widgetId}.filter`;
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
      defaultValues: props.filterModel
    };
    this.formName = generateFormName(props);
    this.handleChangeModel = this.handleChangeModel.bind(this);
    this.handleFilter = this.handleFilter.bind(this);
    this.handleReset = this.handleReset.bind(this);
  }

  getChildContext() {
    return {
      _widgetFilter: {
        formName: this.formName,
        changeModel: this.handleChangeModel,
        filter: this.handleFilter,
        reset: this.handleReset
      }
    };
  }

  componentWillUnmount() {
    const { widgetId, clearFilterModel } = this.props;
    clearFilterModel(widgetId);
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
        defaultValues: filterModel
      }));
    }
  }

  handleChangeModel(values) {
    const { widgetId, filterModel, setFilterModel } = this.props;
    if (!isEqual(filterModel, values)) {
      setFilterModel(widgetId, values);
    }
  }

  handleFilter() {
    const { widgetId, fetchWidget } = this.props;
    fetchWidget(widgetId, { page: 1 });
  }

  handleReset() {
    const {
      fieldsets,
      blackResetList,
      widgetId,
      reduxFormFilter,
      setFilterModel,
      fetchWidget,
      resetFilterModel
    } = this.props;
    let newReduxForm = { ...reduxFormFilter };
    const toReset = difference(map(flatFields(fieldsets, []), 'id'), blackResetList);
    toReset.forEach(field => {
      delete newReduxForm[field];
    });
    this.setState(
      {
        defaultValues: newReduxForm
      },
      () => {
        if (isEmpty(newReduxForm)) {
          resetFilterModel(this.formName);
        }
        setFilterModel(widgetId, newReduxForm);
        fetchWidget(widgetId);
      }
    );
  }

  render() {
    const { fieldsets, visible, hideButtons } = this.props;
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
  clearFilterModel: PropTypes.func,
  setFilterModel: PropTypes.func,
  reduxFormFilter: PropTypes.func,
  fetchWidget: PropTypes.func,
  hideButtons: PropTypes.bool
};

WidgetFilters.defaultProps = {
  hideButtons: false
};

WidgetFilters.childContextTypes = {
  _widgetFilter: PropTypes.object.isRequired
};

const mapStateToProps = createStructuredSelector({
  filterModel: (state, props) => makeGetFilterModelSelector(props.widgetId)(state, props),
  visible: (state, props) => makeWidgetFilterVisibilitySelector(props.widgetId)(state, props),
  reduxFormFilter: (state, props) => getFormValues(generateFormName(props))(state) || {}
});

const mapDispatchToProps = dispatch => {
  return {
    setFilterModel: (widgetId, data) => dispatch(setModel(PREFIXES.filter, widgetId, data)),
    fetchWidget: (widgetId, options) => dispatch(dataRequestWidget(widgetId, options)),
    clearFilterModel: widgetId => dispatch(removeModel(PREFIXES.filter, widgetId)),
    resetFilterModel: formName => dispatch(reset(formName))
  };
};

WidgetFilters = connect(
  mapStateToProps,
  mapDispatchToProps
)(WidgetFilters);

export default WidgetFilters;
