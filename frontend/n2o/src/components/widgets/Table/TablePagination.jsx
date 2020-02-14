import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { connect } from 'react-redux';
import { createStructuredSelector } from 'reselect';
import Pagination from '../../snippets/Pagination/Pagination';
import {
  makeWidgetCountSelector,
  makeWidgetSizeSelector,
  makeWidgetPageSelector
} from '../../../selectors/widgets';
import { dataRequestWidget, changePageWidget } from '../../../actions/widgets';

/**
 * Компонент табличной пейджинации. По `widgetId` автоматически определяет все свойства для `Paging`
 * @reactProps {string} widgetId - уникальный идентификатор виджета
 * @reactProps {number} count
 * @reactProps {number} size
 * @reactProps {number} activePage
 * @reactProps {function} onChangePage
 */
class TablePagination extends Component {
  render() {
    const { count, size, activePage, onChangePage } = this.props;

    return (
      <Pagination
        onSelect={onChangePage}
        activePage={activePage}
        count={count}
        size={size}
        maxButtons={4}
        stepIncrement={10}
      />
    );
  }
}

TablePagination.propTypes = {
  widgetId: PropTypes.string,
  count: PropTypes.number,
  size: PropTypes.number,
  activePage: PropTypes.number,
  onChangePage: PropTypes.func
};

TablePagination.defaultProps = {};

const mapStateToProps = createStructuredSelector({
  count: (state, props) => makeWidgetCountSelector(props.widgetId)(state, props),
  size: (state, props) => makeWidgetSizeSelector(props.widgetId)(state, props),
  activePage: (state, props) => makeWidgetPageSelector(props.widgetId)(state, props)
});

function mapDispatchToProps(dispatch, ownProps) {
  return {
    onChangePage: page => {
      dispatch(
        dataRequestWidget(ownProps.widgetId, {
          page
        })
      );
    }
  };
}

TablePagination = connect(
  mapStateToProps,
  mapDispatchToProps
)(TablePagination);
export default TablePagination;
