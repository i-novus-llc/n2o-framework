import React, { Component } from 'react';
import PropTypes from 'prop-types';
import AdvancedTableContainer from './AdvancedTableContainer';
//TODO AdvancedTablePagination
import StandardWidget from '../StandardWidget';
import Fieldsets from '../Form/fieldsets';

import dependency from '../../../core/dependency';
import { values } from 'lodash';

class AdvancedTableWidget extends Component {
  getWidgetProps() {
    const { headers, cells, sorting, hasFocus, hasSelect, autoFocus, rowColor } = this.props.table;
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
      dataProvider
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
        // filter={this.prepareFilters()}
        // bottomLeft={paging && <AdvancedTablePagination/>}
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

AdvancedTableWidget.propTypes = {};

export default dependency(AdvancedTableWidget);
