import React from "react";
import { Table } from "antd";
import { compose, defaultProps, withHandlers, withProps } from "recompose";
import {
  withWidgetContainer,
  withWidgetHandlers,
  withContainerLiveCycle
} from "n2o/lib/components/widgets/Table/TableContainer";

export default compose(
  withWidgetContainer,
  withWidgetHandlers,
  withContainerLiveCycle
)(Table);
