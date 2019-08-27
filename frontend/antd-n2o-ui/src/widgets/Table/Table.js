import React from "react";
import PropTypes from "prop-types";
import { Table } from "antd";
import { values, pick, map, find } from "lodash";
import { compose, withProps } from "recompose";
import {
  withWidgetContainer,
  withWidgetHandlers,
  withContainerLiveCycle
} from "n2o-framework/lib/components/widgets/Table/TableContainer";

export default compose(
  withWidgetContainer,
  withWidgetHandlers,
  withContainerLiveCycle,
  withProps(props => {
    return {
      columns: map(props.headers, item => ({
        title: item.label,
        key: item.id,
        dataIndex: item.id
      })),
      dataSource: props.datasource
    };
  })
)(Table);
