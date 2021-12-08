import React from "react";
import PropTypes from "prop-types";
import { map } from "lodash";
import factoryResolver from "n2o-framework/lib/core/factory/factoryResolver";
import StandardWidget from "n2o-framework/lib/components/widgets/StandardWidget";

import DataGridContainer from "./DataGridContainer";

/**
 * Кастом видежт датагрид
 */
class DataGridWidget extends React.Component {
  getWidgetProps() {
    const { sorting, hasFocus, hasSelect, autoFocus } = this.props.table;
    const { toolbar, actions, dataProvider, filterDefaultValues } = this.props;
    return {
      columns: this.getColumns(),
      filterDefaultValues,
      sorting,
      toolbar,
      actions,
      hasFocus,
      hasSelect,
      autoFocus,
      dataProvider
    };
  }

  getColumns() {
    const { headers } = this.props.table;
    return map(headers, h => ({
      key: h.id,
      name: h.label,
      resizable: true
    }));
  }

  prepareFilters() {
    return factoryResolver(this.props.filter, "Input");
  }

  render() {
    const {
      id: widgetId,
      toolbar,
      disabled,
      actions,
    } = this.props;
    return (
      <StandardWidget
        disabled={disabled}
        widgetId={widgetId}
        toolbar={toolbar}
        actions={actions}
        filter={this.prepareFilters()}
      >
        <div style={{ marginTop: 10 }}>
          <DataGridContainer
            widgetId={widgetId}
            size={1000}
            {...this.getWidgetProps()}
          />
        </div>
      </StandardWidget>
    );
  }
}

DataGridWidget.defaultProps = {
  toolbar: {},
  filter: {}
};

export default DataGridWidget;
