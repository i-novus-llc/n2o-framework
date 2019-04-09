import React from "react";
import PropTypes from "prop-types";
import { values, pick } from "lodash";
import TableContainer from './Table';
import dependency from 'n2o/lib/core/dependency';

//TODO: не получилось переделать под HOC.
class TableWidget extends React.Component {
  /**
   * Замена src на компонент
   */
  getWidgetProps() {
    const {
      headers,
      cells,
      sorting,
      hasFocus,
      hasSelect,
      autoFocus,
      rowColor,
      rowClick
    } = this.props.table;
    const { toolbar, actions, dataProvider } = this.props;
    const { resolveProps } = this.context;
    return {
      headers: values(resolveProps(headers)),
      cells: values(resolveProps(cells)),
      sorting,
      toolbar,
      rowColor,
      actions,
      hasFocus,
      hasSelect,
      autoFocus,
      dataProvider,
      rowClick
    };
  }

  render() {
    const {
      id: widgetId,
      table: { fetchOnInit, size },
      pageId
    } = this.props;
    return (
      <TableContainer
        widgetId={widgetId}
        pageId={pageId}
        size={size}
        page={1}
        fetchOnInit={fetchOnInit}
        {...this.getWidgetProps()}
      />
    );
  }
}

TableWidget.defaultProps = {
  toolbar: {},
  filter: {}
};

TableWidget.propTypes = {
  className: PropTypes.string,
  style: PropTypes.object,
  containerId: PropTypes.string.isRequired,
  pageId: PropTypes.string.isRequired,
  widgetId: PropTypes.string,
  actions: PropTypes.object,
  toolbar: PropTypes.object,
  dataProvider: PropTypes.object,
  table: PropTypes.arrayOf(
    PropTypes.shape({
      size: PropTypes.number,
      fetchOnInit: PropTypes.bool,
      hasSelect: PropTypes.bool,
      className: PropTypes.string,
      style: PropTypes.object,
      autoFocus: PropTypes.bool,
      sorting: PropTypes.object,
      headers: PropTypes.array,
      cells: PropTypes.array
    })
  ),
  paging: PropTypes.oneOfType([PropTypes.bool, PropTypes.object])
};

TableWidget.contextTypes = {
  resolveProps: PropTypes.func
};

export default dependency(TableWidget);
