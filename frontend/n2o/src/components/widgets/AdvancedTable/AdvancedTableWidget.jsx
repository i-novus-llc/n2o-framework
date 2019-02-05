import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AdvancedTableContainer from './AdvancedTableContainer';
import AdvancedTablePagination from './AdvancedTablePagination';
import StandardWidget from '../StandardWidget';
import Fieldsets from '../Form/fieldsets';

import dependency from '../../../core/dependency';
import { values } from 'lodash';

class AdvancedTableWidget extends Component {
  getWidgetProps() {
    const {
      headers,
      cells,
      sorting,
      hasFocus,
      hasSelect,
      autoFocus,
      rowColor,
      rowSelection,
      tableSize,
      useFixedHeader,
      expandable,
      scroll
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
      rowSelection,
      tableSize,
      useFixedHeader,
      expandable,
      scroll
    };
  }

  prepareFilters() {
    return this.context.resolveProps(this.props.filter, Fieldsets.StandardFieldset);
  }

  render() {
    const {
      id: widgetId,
      toolbar,
      disabled,
      actions,
      table: { fetchOnInit, size },
      pageId,
      paging
    } = this.props;
    return (
      <StandardWidget
        disabled={disabled}
        widgetId={widgetId}
        toolbar={toolbar}
        actions={actions}
        filter={this.prepareFilters()}
        bottomLeft={paging && <AdvancedTablePagination widgetId={widgetId} />}
      >
        <AdvancedTableContainer
          widgetId={widgetId}
          pageId={pageId}
          size={size}
          page={1}
          fetchOnInit={fetchOnInit}
          {...this.getWidgetProps()}
        />
      </StandardWidget>
    );
  }
}

AdvancedTableWidget.contextTypes = {
  resolveProps: PropTypes.func
};

AdvancedTableWidget.defaultProps = {
  toolbar: {},
  filter: {}
};

AdvancedTableWidget.propTypes = {
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

export default dependency(AdvancedTableWidget);
